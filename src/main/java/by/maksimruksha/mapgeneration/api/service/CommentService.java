package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.CommentDto;
import by.maksimruksha.mapgeneration.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(CommentDto commentDto);
    CommentDto read(Long id);
    CommentDto update(CommentDto commentDto);
    Boolean delete(Long id);
    Page<CommentDto> findAllByMap(Pageable pageable, Long mapId);
    Boolean userCommentExists(Long mapId, Long userId);
    CommentDto findUserComment(Long mapId, Long userId);
    CommentDto send(CommentDto commentDto);
    Long getPagesCount(Long mapId, Long pageSize);
}
