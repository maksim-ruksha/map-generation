package by.maksimruksha.mapgeneration.api.service;

import java.awt.image.BufferedImage;

public interface ImageToBytesService {

    byte[] convert(BufferedImage image);
}
