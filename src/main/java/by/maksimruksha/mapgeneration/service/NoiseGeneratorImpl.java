package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.NoiseGenerator;

public class NoiseGeneratorImpl implements NoiseGenerator {
    private long seed = 0;

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
            noise += getSimpleNoise(scaledX, scaledY);

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
        float value = lerp(downDot, upDot, localY);

        return value;
    }

    // linear interpolation
    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    // dot product of two-dimensional vectors
    private float dot(float[] v1, float[] v2) {
        return v1[0] * v1[1] + v2[0] * v2[0];
    }

    // get random vector based on coords
    private float[] getVertexVector(int x, int y) {
        // inserting seed here makes output vectors seed-dependent
        int vectorIndex = (int) hash((hash(x) + hash(y)) * seed) % 3;
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
