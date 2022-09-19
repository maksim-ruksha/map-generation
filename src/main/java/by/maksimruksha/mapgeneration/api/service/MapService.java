package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.MapDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MapService {
    MapDto create(MapDto map);
    MapDto read(Long id);
    boolean update(MapDto map);
    boolean delete(Long id);
    Page<MapDto> findAll(Pageable pageable);
}
