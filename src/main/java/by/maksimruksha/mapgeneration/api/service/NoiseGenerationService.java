package by.maksimruksha.mapgeneration.api.service;

<<<<<<< HEAD:src/main/java/by/maksimruksha/mapgeneration/api/service/NoiseGenerationService.java
public interface NoiseGenerationService {
=======
public interface NoiseGenerator {
>>>>>>> 2d5e33a42782ebce63647d4c5e8db24e4977d06d:src/main/java/by/maksimruksha/mapgeneration/api/service/NoiseGenerator.java
    void setSeed(long seed);
    void setScale(float scale);
    float getValue(float x, float y);
    float getValue(float x, float y, int octaves);
    float getValue(float x, float y, int octaves, float persistence);
}
