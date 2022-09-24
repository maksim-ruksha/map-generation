package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(CommentDto commentDto);
    CommentDto read(Long id);
    CommentDto update(CommentDto commentDto);
    Boolean delete(Long id);
    Page<CommentDto> findAllByMap(Pageable pageable, Long mapId);
}
