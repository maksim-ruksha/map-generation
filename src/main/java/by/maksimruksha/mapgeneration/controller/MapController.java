package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.MapService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {

    private final MapService mapService;
    private final MapGenerationService mapGenerationService;

    @GetMapping(
            value = "/generate",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] generate(@RequestParam String seed) {
        Long seedNumber = (long) seed.hashCode();
        MapDto map = new MapDto();
        map.setSeed(seedNumber);

        BufferedImage image = mapGenerationService.generate(seedNumber);

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

    @PostMapping("/create")
    public ResponseEntity<MapDto> create(MapDto mapDto)
    {
        MapDto response = mapService.create(mapDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapDto> read(@PathVariable Long id)
    {
        MapDto mapDto = mapService.read(id);
        return ResponseEntity.ok(mapDto);
    }

    @PutMapping("/update")
    public ResponseEntity<MapDto> update(MapDto mapDto)
    {
        MapDto response = mapService.update(mapDto);
        return ResponseEntity.ok(response);
    }
}
