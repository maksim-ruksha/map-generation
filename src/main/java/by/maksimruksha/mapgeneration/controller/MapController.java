package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.MapService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {
    //private final MapService mapService;
    private final MapGenerationService mapGenerationService;

    @GetMapping(
            value = "/generate/{seed}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] generate(@PathVariable Long seed/*, String generator*/) {
        MapDto map = new MapDto();
        map.setSeed(seed);
        System.out.println("GOT SEED: " + seed);
        BufferedImage image = mapGenerationService.generate(seed);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

}
