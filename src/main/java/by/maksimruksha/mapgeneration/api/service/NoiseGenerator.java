package by.maksimruksha.mapgeneration.api.service;

public interface NoiseGenerator {

    void setSeed(long seed);
    float getValue(float x, float y);

    float getValue(float x, float y, int octaves);

    float getValue(float x, float y, int octaves, float persistence);

}
