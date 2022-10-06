package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.service.ImageToBytesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class ImageToBytesServiceImpl implements ImageToBytesService {

    @Override
    public byte[] convert(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
}
