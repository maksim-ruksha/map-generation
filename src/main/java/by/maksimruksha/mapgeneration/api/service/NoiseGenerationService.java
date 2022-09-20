package by.maksimruksha.mapgeneration.api.service;

public interface NoiseGenerationService {
    void setSeed(long seed);
    void setScale(float scale);
    float getValue(float x, float y);
    float getValue(float x, float y, int octaves);
    float getValue(float x, float y, int octaves, float persistence);
}
