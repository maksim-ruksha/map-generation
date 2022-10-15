package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.NoiseGenerationService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class MinecraftyMapGenerationImpl implements MapGenerationService {

    // biome colors
    private final Color BIOME_COLOR_DESERT = Color.decode("#e0dd80"); // hot and dry
    private final Color BIOME_COLOR_PLAINS = Color.decode("#368733"); // mid temperature, mid humidity
    private final Color BIOME_COLOR_SNOW = Color.decode("#cadbed"); // cold and wet AND also replaces beach in cold zones (index 0)
    private final Color BIOME_COLOR_TAIGA = Color.decode("#164236"); // cold with mid humidity
    private final Color BIOME_COLOR_COLDLANDS = Color.decode("#374057"); // cold and dry
    private final Color BIOME_COLOR_WARMLANDS = Color.decode("#688733"); // mid tempearture with low humidity
    private final Color BIOME_COLOR_FOREST = Color.decode("#144715"); // mid temperature with mid humidity
    private final Color BIOME_COLOR_JUNGLE = Color.decode("#022602"); // high temperature, high humidity
    private final Color BIOME_COLOR_SAVANNA = Color.decode("#d6712d"); // high temperature, mid humidity
    private final Color BIOME_COLOR_BEACH = Color.decode("#b8b333"); // spawns before water level (below BEACH_GLOBAL_HEIGHT)
    private final Color BIOME_COLOR_WATER = Color.decode("#2d46b5"); // spawns below WATER_GLOBAL_HEIGHT
    private final Color BIOME_COLOR_ICE = Color.decode("#3c6c91"); // replaces water in cold zones (index 0) (also spawns below WATER_GLOBAL_HEIGHT)

    // more weirder = more noisier
    private final int WEIRD_HEIGHT_OCTAVES = 8;

    // less weirder = more smooth
    private final int NORMAL_HEIGHT_OCTAVES = 3;

    // less octaves = more smooth and staright rivers
    private final int RIVERS_OCTAVES = 6;

    // controls index 0 (0 <= index 0 < NOISE_LEVEL_LOW_HEIGHT)
    private final float NOISE_LEVEL_LOW_HEIGHT = 0.333f;

    // controls index 1 (NOISE_LEVEL_LOW_HEIGHT <= index 0 < NOISE_LEVEL_MID_HEIGHT), NOISE_LEVEL_MID_HEIGHT < index 2 < 1
    private final float NOISE_LEVEL_MID_HEIGHT = 0.5f;

    // index 0, 1, 2
    private final int NOISE_LEVEL_LOW = 0;
    private final int NOISE_LEVEL_MID = 1;
    private final int NOISE_LEVEL_HIGH = 2;

    // controls world ocean height
    private final float WATER_GLOBAL_HEIGHT = 0.0734f;

    // controls beach height
    private final float BEACH_GLOBAL_HEIGHT = WATER_GLOBAL_HEIGHT + 0.015f;

    // temperature-humidity biome table
    // temperature -->
    // humidity \/
    private final Color[][] BIOME_TEMPERATURE_HUMIDITY_TABLE = {
            {BIOME_COLOR_COLDLANDS, BIOME_COLOR_WARMLANDS, BIOME_COLOR_DESERT},
            {BIOME_COLOR_TAIGA, BIOME_COLOR_PLAINS, BIOME_COLOR_SAVANNA},
            {BIOME_COLOR_SNOW, BIOME_COLOR_FOREST, BIOME_COLOR_JUNGLE},
    };

    // controls affection of height on temperature, higher = colder
    private final float HEIGHT_ON_TEMPERATURE_AFFECTION = 0.73f;

    // controls affection of height on humidity, higher = dryer
    private final float HEIGHT_ON_HUMIDITY_AFFECTION = 0.35f;

    // rivers noise scale, means how often rivers will spawn
    private final float RIVERS_SCALE = 0.25f;

    // weirdness noise scale, means how often weirdness will change
    private final float WEIRDNESS_SCALE = 0.35f;

    // humidity noise scale, means how often humidity will change
    private final float HUMIDITY_SCALE = 0.25f;

    // temperature noise scale, means how often temperature will change
    private final float TEMPERATURE_SCALE = 0.125f;

    // controls thiccness of rivers
    private final float RIVERS_THICKNESS = 0.01f;

    // minimum size of image
    private final int MIN_IMAGE_SIZE = 256;

    // max image size multiplier
    private final int MAX_LOD = 5;

    // how many space of landscape we want to capture
    private final float MAP_SCALE = 10.0f;

    // light direction multiplied by -1, used for lighting calculation
    /*   \
     *    \
     *    _\|
     */
    private final float[] INVERSE_LIGHT_DIRECTION = normalize(new float[]{-0.5f, 0.5f, -0.5f}); // LIGHT_DIRECTION = {0.5f, -0.5f, 0.5f}

    // maximum amount of direct light
    private final float DIRECT_LIGHT_INTENSITY = 1.0f;

    // constant amount of light which will be received in any condition
    private final float AMBIENT_LIGHT_INTENSITY = 0.4f;

    // noise generators
    private final NoiseGenerationService riversNoiseGenerationService;
    private final NoiseGenerationService weirdnessNoiseGenerationService;
    private final NoiseGenerationService heightNoiseGenerationService;
    private final NoiseGenerationService temperatureNoiseGenerationService;
    private final NoiseGenerationService humidityNoiseGenerationService;

    @Override
    public BufferedImage generate(MapDto map) {
        return generate(map.getSeed(), 1);
    }

    @Override
    public BufferedImage generate(String seed) {
        return generate(seed, 1);
    }

    @Override
    public BufferedImage generate(String seed, int lod) {
        setSeeds(seed);
        lod = Math.min(MAX_LOD, lod);
        int imageSize = MIN_IMAGE_SIZE * lod;
        int[] pixels = getPixels(imageSize, imageSize);
        BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, imageSize, imageSize, pixels, 0, image.getWidth());
        return image;
    }

    // set seeds
    private void setSeeds(String seed) {
        riversNoiseGenerationService.setSeed((seed + "_riversNoise").hashCode());
        weirdnessNoiseGenerationService.setSeed((seed + "_weirdnessNoise").hashCode());
        heightNoiseGenerationService.setSeed((seed + "_heightNoise").hashCode());
        temperatureNoiseGenerationService.setSeed((seed + "_temperatureNoise").hashCode());
        humidityNoiseGenerationService.setSeed((seed + "_humidityNoise").hashCode());
    }

    private int[] getPixels(int width, int height) {
        int pixelsCount = width * height;
        int[] pixels = new int[pixelsCount];

        float deltaX = 1.0f / width;
        float deltaY = 1.0f / height;

        // iterate through pixels
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                // calculate float coords
                float u = x * deltaX * MAP_SCALE;
                float v = y * deltaY * MAP_SCALE;

                // get color
                int color = getColor(u, v, width, height).getRGB();
                pixels[x * height + y] = color;
            }
        }
        return pixels;
    }

    private Color getColor(float u, float v, int width, int height) {
        // get height
        float mapHeight = getHeight(u, v);

        // get temperature and apply height on temperature affection
        float temperature = getTemperature(u, v) * (1 - mapHeight * HEIGHT_ON_TEMPERATURE_AFFECTION);

        // get humidity and apply height on humidity affection
        float humidity = getHumidity(u, v) * (1 - mapHeight * HEIGHT_ON_HUMIDITY_AFFECTION);

        // get biome color
        Color biomeColor = getBiomeColor(mapHeight, temperature, humidity);

        // don't apply light to water and ice beacuse it will look nicer
        if (biomeColor.equals(BIOME_COLOR_ICE) || biomeColor.equals(BIOME_COLOR_WATER)) {
            return biomeColor;
        }

        // calculate light
        float light = saturate(getLight(u, v, width, height));

        // apply light
        biomeColor = new Color(
                (int) Math.floor(biomeColor.getRed() * light),
                (int) Math.floor(biomeColor.getGreen() * light),
                (int) Math.floor(biomeColor.getBlue() * light)
        );

        return biomeColor;
    }

    // helper function to avoid overflowing the values
    private float saturate(float a) {
        if (a > 1)
            return 1;
        if (a < 0)
            return 0;
        return a;
    }

    private float getLight(float u, float v, int width, int height) {
        // calculate texel size
        float uOffset = 1.0f / width;
        float vOffset = 1.0f / height;

        // can be received from outside
        float center = getHeight(u, v);

        // calculate neighbour heights
        float right = getHeight(u + uOffset, v);
        float top = getHeight(u, v + vOffset);

        // calculate height differences from center
        float rightDelta = right - center;
        float topDelta = top - center;

        // calculate normals (surface perpendiculars)
        float[] normalRight = normalize(new float[]{0, rightDelta, uOffset});
        float[] normalTop = normalize(new float[]{vOffset, topDelta, 0});

        // combine normals into one
        float[] normal = normalize(new float[]{
                normalRight[0] + normalTop[0],
                normalRight[1] + normalTop[1],
                normalRight[2] + normalTop[2],
        });

        // calculate amount of direct light
        // if normal equals INVERSE_LIGHT_DIRECTION then directLight will be DIRECT_LIGHT_INTENSITY
        // if normal equals -INVERSE_LIGHT_DIRECTION then directLight will be 0
        float directLight = (dot(INVERSE_LIGHT_DIRECTION, normal) * 0.5f + 0.5f) * DIRECT_LIGHT_INTENSITY;
        // add ambient light, this will prevent surface from going completly black
        float light = directLight + AMBIENT_LIGHT_INTENSITY;
        return light;
    }

    // dot product of 3d vectors
    private float dot(float[] v1, float[] v2) {
        return v1[0] * v2[0]
                + v1[1] * v2[1]
                + v1[2] * v2[2];
    }

    // normalizes 3d vector
    private float[] normalize(float[] v) {
        float magnitude = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        float multiplier = 1.0f / magnitude;
        return new float[]{v[0] * multiplier, v[1] * multiplier, v[2] * multiplier};
    }

    private Color getBiomeColor(float height, float temperature, float humidity) {
        // get temperature index
        int temperatureLevel = getNoiseLevel(temperature);

        // get humidity index
        int humidityLevel = getNoiseLevel(humidity);

        // water checks
        if (temperatureLevel > 0) {
            // water and beach
            if (height < WATER_GLOBAL_HEIGHT) {
                return BIOME_COLOR_WATER;
            }
            if (height < BEACH_GLOBAL_HEIGHT) {
                return BIOME_COLOR_BEACH;
            }
        } else {
            // ice and snow
            if (height < WATER_GLOBAL_HEIGHT) {
                return BIOME_COLOR_ICE;
            }
            if (height < BEACH_GLOBAL_HEIGHT) {
                return BIOME_COLOR_SNOW;
            }
        }

        // get biome with corresponding indexes from table
        Color biomeColor = BIOME_TEMPERATURE_HUMIDITY_TABLE[humidityLevel][temperatureLevel];
        return biomeColor;
    }

    // linear interpolation
    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    // cosine curve
    private float cosine(float t) {
        return (float) ((1 - Math.cos(t * Math.PI)) * 0.5);
    }

    private float getRivers(float u, float v) {
        u *= RIVERS_SCALE;
        v *= RIVERS_SCALE;
        // sample noise
        float noise = riversNoiseGenerationService.getValue(u, v, RIVERS_OCTAVES);
        // calculate rivers factor
        float deltaToMid = Math.abs(0.5f - noise) * 2 / RIVERS_THICKNESS;
        return 1 - saturate(deltaToMid);
    }

    private float getWeirdness(float u, float v) {
        u *= WEIRDNESS_SCALE;
        v *= WEIRDNESS_SCALE;
        // sample noise
        return weirdnessNoiseGenerationService.getValue(u, v);
    }

    private float getHeight(float u, float v) {
        // sample weird variant
        float weirdHeight = heightNoiseGenerationService.getValue(u, v, WEIRD_HEIGHT_OCTAVES);
        // sample normal variant
        float normalHeight = heightNoiseGenerationService.getValue(u, v, NORMAL_HEIGHT_OCTAVES);
        // rivers factor
        float rivers = getRivers(u, v);

        // combine weird and normal variant according to weirdness factor
        float height = lerp(normalHeight, weirdHeight, cosine(getWeirdness(u, v)));
        // apply rivers factor
        // cosines make transitions nicer
        return lerp(height * height * height, 0, cosine(rivers));
    }

    //sample temperature noise
    private float getTemperature(float u, float v) {
        u *= TEMPERATURE_SCALE;
        v *= TEMPERATURE_SCALE;
        float temperature = temperatureNoiseGenerationService.getValue(u, v);
        return temperature;
    }

    // sample humidity noise
    private float getHumidity(float u, float v) {
        u *= HUMIDITY_SCALE;
        v *= HUMIDITY_SCALE;
        float humidity = humidityNoiseGenerationService.getValue(u, v);
        return humidity;
    }

    // calculate index
    private int getNoiseLevel(float height) {
        if (height < NOISE_LEVEL_LOW_HEIGHT) {
            return NOISE_LEVEL_LOW;
        }
        if (height < NOISE_LEVEL_MID_HEIGHT) {
            return NOISE_LEVEL_MID;
        }
        return NOISE_LEVEL_HIGH;
    }
}
