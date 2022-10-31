package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.LikeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    LikeDto create(LikeDto like);
    LikeDto read(Long id);
    LikeDto update(LikeDto like);
    Boolean delete(Long userId, Long mapId);
    Boolean userLikeExists(Long userId, Long mapId);
    Page<LikeDto> findAll(Pageable pageable);
    Page<LikeDto> findAllByMap(Pageable pageable, Long likeId);
    Long countAllByMap(Long mapId);
}
