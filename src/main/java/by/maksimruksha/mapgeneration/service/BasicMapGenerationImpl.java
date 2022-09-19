package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.NoiseGenerationService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class BasicMapGenerationImpl implements MapGenerationService {
    private final NoiseGenerationService noiseGenerationService;
    
    private final int IMAGE_SIZE = 512;

    @Override
    public BufferedImage generate(MapDto map) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        noiseGenerationService.setSeed(map.getSeed());
        int[] pixels = getPixels(IMAGE_SIZE, IMAGE_SIZE);
        image.setRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE, pixels, 0, IMAGE_SIZE);
        return image;
    }

    @Override
    public BufferedImage generate(Long seed) {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        noiseGenerationService.setSeed(seed);
        noiseGenerationService.setScale(50);
        int[] pixels = getPixels(IMAGE_SIZE, IMAGE_SIZE);
        image.setRGB(0, 0, IMAGE_SIZE, IMAGE_SIZE, pixels, 0, image.getWidth());
        return image;
    }

    private int[] getPixels(int width, int height) {
        int pixelsCount = width * height;
        int[] pixels = new int[pixelsCount];

        float deltaX = 1.0f / width;
        float deltaY = 1.0f / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float u = x * deltaX;
                float v = y * deltaY;

                float noise = noiseGenerationService.getValue(u, v);
                int color = new Color(noise, noise, noise).getRGB();
                pixels[x * height + y] = color;
            }
        }
        return pixels;
    }
}
