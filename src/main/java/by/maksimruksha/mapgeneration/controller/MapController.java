package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.ImageToBytesService;
import by.maksimruksha.mapgeneration.api.service.MapGenerationService;
import by.maksimruksha.mapgeneration.api.service.MapService;
import by.maksimruksha.mapgeneration.dto.MapDto;
import by.maksimruksha.mapgeneration.util.SortHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/maps")
public class MapController {

    private final MapService mapService;
    private final MapGenerationService mapGenerationService;

    private final ImageToBytesService imageToBytesService;

    @GetMapping(
            value = "/generate/{seed}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody byte[] generate(@RequestParam String seed) {
        Long seedNumber = (long) seed.hashCode();
        MapDto map = new MapDto();
        map.setSeed(seedNumber);
        BufferedImage image = mapGenerationService.generate(seedNumber);
        return imageToBytesService.convert(image);
    }

    @GetMapping("/")
    public ResponseEntity<List<MapDto>> getAll(@RequestParam(required = false, defaultValue = "id") String sortField,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "asc") String direction,
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(SortHelper.orderDirection(direction), sortField));
        Page<MapDto> maps = mapService.findAll(pageable);
        return ResponseEntity.ok(maps.getContent());
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
