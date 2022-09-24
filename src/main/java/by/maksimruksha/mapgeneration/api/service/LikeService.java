package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.LikeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    LikeDto create(LikeDto map);
    LikeDto read(Long id);
    LikeDto update(LikeDto map);
    Boolean delete(Long id);
    Page<LikeDto> findAll(Pageable pageable);
    Page<LikeDto> findAllByMap(Pageable pageable, Long mapId);
    Long countAllByMap(Long mapId);
}
