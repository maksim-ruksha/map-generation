package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.NoiseGenerationService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class MinecraftyMapGenerationImpl implements MapGenerationService {

    private final Color BIOME_COLOR_DESERT = Color.decode("#e0dd80"); // high temperatur, low humidity
    private final Color BIOME_COLOR_PLAINS = Color.decode("#368733"); // mid temperature, mid humidity
    private final Color BIOME_COLOR_SNOW = Color.decode("#cadbed");// low temperature, high humidity
    private final Color BIOME_COLOR_TAIGA = Color.decode("#164236");
    private final Color BIOME_COLOR_COLDLANDS = Color.decode("#374057"); // low temperature, low humidity
    private final Color BIOME_COLOR_COLDFOREST = Color.decode("#144547");
    private final Color BIOME_COLOR_WARMLANDS = Color.decode("#688733");
    private final Color BIOME_COLOR_FOREST = Color.decode("#144715");
    private final Color BIOME_COLOR_JUNGLE = Color.decode("#026115"); // high temperature, high humidity
    private final Color BIOME_COLOR_SAVANNA = Color.decode("#d6712d"); // high temperature, mid humidity
    private final Color BIOME_COLOR_BEACH = Color.decode("#b8b333"); // low height, but little higher, than for water
    private final Color BIOME_COLOR_WATER = Color.decode("#2d46b5"); // low height, mid or higher humidity
    private final Color BIOME_COLOR_ICE = Color.decode("#3c6c91");


    private final float NOISE_LEVEL_LOW_HEIGHT = 0.333f;
    private final float NOISE_LEVEL_MID_HEIGHT = 0.5f;
    private final int NOISE_LEVEL_LOW = 0;
    private final int NOISE_LEVEL_MID = 1;
    private final int NOISE_LEVEL_HIGH = 2;

    private final float WATER_GLOBAL_HEIGHT = 0.19f;
    private final float BEACH_GLOBAL_HEIGHT = 0.20f;

    // temperature -->
    // humidity \/
    private final Color[][] BIOME_TEMPERATURE_HUMIDITY_TABLE = {
            {BIOME_COLOR_COLDLANDS, BIOME_COLOR_WARMLANDS, BIOME_COLOR_DESERT},
            {BIOME_COLOR_TAIGA, BIOME_COLOR_PLAINS, BIOME_COLOR_SAVANNA},
            {BIOME_COLOR_SNOW, BIOME_COLOR_FOREST, BIOME_COLOR_JUNGLE},
    };

    private final float HUMIDITY_SCALE = 0.25f;
    private final float TEMPERATURE_SCALE = 0.125f;

    private final int MIN_IMAGE_SIZE = 256;
    private final int MAX_LOD = 5;

    private final float MAP_SCALE = 10.0f;

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

    private void setSeeds(String seed) {
        heightNoiseGenerationService.setSeed((seed + "_heightNoise").hashCode());
        temperatureNoiseGenerationService.setSeed((seed + "_temperatureNoise").hashCode());
        humidityNoiseGenerationService.setSeed((seed + "_humidityNoise").hashCode());
    }

    private int[] getPixels(int width, int height) {
        int pixelsCount = width * height;
        int[] pixels = new int[pixelsCount];

        float deltaX = 1.0f / width;
        float deltaY = 1.0f / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float u = x * deltaX * MAP_SCALE;
                float v = y * deltaY * MAP_SCALE;

                int color = getColor(u, v).getRGB();
                pixels[x * height + y] = color;
            }
        }
        return pixels;
    }

    private Color getColor(float u, float v) {
        float height = getHeight(u, v);
        float temperature = getTemperature(u, v) * (height + 0.5f);
        float humidity = getHumidity(u, v) * (height + 0.5f);

        Color biomeColor = getBiomeColor(height, temperature, humidity);
        return biomeColor;
    }

    private Color getBiomeColor(float height, float temperature, float humidity) {
        int temperatureLevel = getNoiseLevel(temperature);
        int humidityLevel = getNoiseLevel(humidity);

        if (temperatureLevel > 0) {
            if (height < WATER_GLOBAL_HEIGHT) {
                return BIOME_COLOR_WATER;
            }
            if (height < BEACH_GLOBAL_HEIGHT) {
                return BIOME_COLOR_BEACH;
            }
        } else {
            if (height < WATER_GLOBAL_HEIGHT) {
                return BIOME_COLOR_ICE;
            }
            if (height < BEACH_GLOBAL_HEIGHT) {
                return BIOME_COLOR_SNOW;
            }
        }
        // for debugging
        // return new Color(temperatureLevel / 2.0f, humidityLevel / 2.0f, 0);

        Color biomeColor = BIOME_TEMPERATURE_HUMIDITY_TABLE[humidityLevel][temperatureLevel];
        return biomeColor;
    }

    private float getHeight(float u, float v) {
        float height = heightNoiseGenerationService.getValue(u, v, 8);
        float noise = heightNoiseGenerationService.getValue(u, v, 8);
        return height * noise;
    }

    private float getTemperature(float u, float v) {
        u *= TEMPERATURE_SCALE;
        v *= TEMPERATURE_SCALE;
        float temperature = temperatureNoiseGenerationService.getValue(u, v);
        return temperature;
    }

    private float getHumidity(float u, float v) {
        u *= HUMIDITY_SCALE;
        v *= HUMIDITY_SCALE;
        float humidity = humidityNoiseGenerationService.getValue(u, v);
        return humidity;
    }

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
