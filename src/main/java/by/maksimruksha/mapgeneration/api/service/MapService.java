package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.MapDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MapService {
    MapDto create(MapDto mapDto);
    MapDto read(Long id);
    MapDto update(MapDto mapDto);
    boolean delete(Long id);
    Page<MapDto> findAll(Pageable pageable);
    Page<MapDto> findAllByAuthor(Pageable pageable, Long userId);
}
