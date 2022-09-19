package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.NoiseGenerationService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

<<<<<<< HEAD:src/main/java/by/maksimruksha/mapgeneration/service/NoiseGenerationServiceImpl.java
@Service
public class NoiseGenerationServiceImpl implements NoiseGenerationService {

=======
public class NoiseGeneratorImpl implements NoiseGenerator {
>>>>>>> 2d5e33a42782ebce63647d4c5e8db24e4977d06d:src/main/java/by/maksimruksha/mapgeneration/service/NoiseGeneratorImpl.java
    private long seed = 0;
    private float scale = 1;

    private static final float[][] VERTEX_VECTORS =
            {
                    new float[]{1, 0},
                    new float[]{0, 1},
                    new float[]{-1, 0},
                    new float[]{0, -1},
            };

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    // just get the noise
    public float getValue(float x, float y) {
        return getSimpleNoise(x, y);
    }

    // assuming persistence is 0.5
    public float getValue(float x, float y, int octaves) {
        return getValue(x, y, octaves, 0.5f);
    }

    // noise
    public float getValue(float x, float y, int octaves, float persistence) {
        float noise = 0.0f;

        float totalWeight = 0.0f;
        float scaledX = x;
        float scaledY = y;
        float currentNoiseWeight = 1.0f;

        for (int i = 0; i < octaves; i++) {
            // add octave noise
            noise += getSimpleNoise(scaledX, scaledY) * currentNoiseWeight;

            // collect weight
            totalWeight += currentNoiseWeight;

            // make next octave weaker
            currentNoiseWeight *= persistence;

            // increase noise scale
            scaledX *= 2;
            scaledY *= 2;
        }

        // normalize noise value
        noise /= totalWeight;

        return noise;
    }

    private float getSimpleNoise(float x, float y) {

        // scale coords
        x *= scale;
        y *= scale;

        // calculate cell vertices coords
        int cellStartX = (int) Math.floor(x);
        int cellStartY = (int) Math.floor(y);
        int cellEndX = cellStartX + 1;
        int cellEndY = cellStartY + 1;

        // calculate cell-space coords
        float localX = x - cellStartX;
        float localY = y - cellStartY;

        // get pseudo random vectors to calculate dot product with
        // x is pointing right
        // y is pointing up
        float[] downLeftVector = getVertexVector(cellStartX, cellStartY);
        float[] downRightVector = getVertexVector(cellEndX, cellStartY);
        float[] upLeftVector = getVertexVector(cellStartX, cellEndY);
        float[] upRightVector = getVertexVector(cellEndX, cellEndY);

        // calculate vectors pointing to point inside cell from vertices
        float[] fromDownLeft = {localX, localY};
        float[] fromDownRight = {localX - 1, localY};
        float[] fromUpLeft = {localX, localY - 1};
        float[] fromUpRight = {localX - 1, localY - 1};

        // get dot products of vertices
        float downLeftDot = dot(downLeftVector, fromDownLeft);
        float downRightDot = dot(downRightVector, fromDownRight);
        float upLeftDot = dot(upLeftVector, fromUpLeft);
        float upRightDot = dot(upRightVector, fromUpRight);

        // interpolate between dot products based on cell-space coords
        float downDot = lerp(downLeftDot, downRightDot, localX);
        float upDot = lerp(upLeftDot, upRightDot, localX);

        // final result
<<<<<<< HEAD:src/main/java/by/maksimruksha/mapgeneration/service/NoiseGenerationServiceImpl.java
        float value = lerp(downDot, upDot, localY);
        value = value * 0.5f + 0.5f;
=======
        float noise = lerp(downDot, upDot, localY);
        // arrange noise from 0 to 1
        noise = noise * 0.5f + 0.5f;
>>>>>>> 2d5e33a42782ebce63647d4c5e8db24e4977d06d:src/main/java/by/maksimruksha/mapgeneration/service/NoiseGeneratorImpl.java

        return noise;
    }

    // linear interpolation
    private float lerp(float a, float b, float t) {
        return a + (b - a) * cosine(t);
    }

    // makes noise less blocky
    private float cosine(float t) {
        return (float) ((1 - Math.cos(t * Math.PI)) * 0.5);
    }

    private float[] normalize(float[] v) {
        float magnitude = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1]);
        return new float[]{v[0] / magnitude, v[1] / magnitude};
    }

    // dot product of two-dimensional vectors
    private float dot(float[] v1, float[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1];
    }

    // get random vector based on coords
    private float[] getVertexVector(int x, int y) {
        // inserting seed here makes output vectors seed-dependent
        int vectorIndex = (int) Math.abs(hash(hash(x) + hash(y) + hash(seed + x * y)) & 3);
        return VERTEX_VECTORS[vectorIndex];
    }

    // hash
    private long hash(long a) {
        a ^= (a << 13);
        a ^= (a >>> 17);
        a ^= (a << 5);
        return a;
    }
}
