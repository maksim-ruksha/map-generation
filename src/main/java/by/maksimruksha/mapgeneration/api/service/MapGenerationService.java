package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.MapDto;

import java.awt.image.BufferedImage;

public interface MapGenerationService {
    BufferedImage generate(MapDto map);

    BufferedImage generate(String seed);
    BufferedImage generate(String seed, int lod);
}
