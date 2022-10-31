package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.repository.CommentRepository;
import by.maksimruksha.mapgeneration.api.repository.MapRepository;
import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.api.service.CommentService;
import by.maksimruksha.mapgeneration.dto.CommentDto;
import by.maksimruksha.mapgeneration.entities.Comment;
import by.maksimruksha.mapgeneration.entities.Map;
import by.maksimruksha.mapgeneration.entities.User;
import by.maksimruksha.mapgeneration.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MapRepository mapRepository;

    private final ModelMapper mapper;

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto) {
        Long userId = commentDto.getAuthor().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

        Long mapId = commentDto.getMap().getId();
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));

        Comment comment = mapper.map(commentDto, Comment.class);

        comment.setAuthor(user);
        comment.setMap(map);

        Comment response = commentRepository.save(comment);
        return mapper.map(response, CommentDto.class);
    }

    @Override
    @Transactional
    public CommentDto read(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment " + id + " not found."));
        return mapper.map(comment, CommentDto.class);
    }

    @Override
    @Transactional
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentRepository.save(mapper.map(commentDto, Comment.class));
        return mapper.map(comment, CommentDto.class);
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<CommentDto> findAllByMap(Pageable pageable, Long mapId) {
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));
        return commentRepository
                .findAllByMap(pageable, map)
                .map(comment -> mapper.map(comment, CommentDto.class));
    }

    @Override
    public Boolean userCommentExists(Long mapId, Long userId) {
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));
        User author = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return commentRepository.existsByMapAndAuthor(map, author);
    }

    @Override
    public CommentDto findUserComment(Long mapId, Long userId) {
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));
        User author = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return mapper.map(commentRepository.findFirstByMapAndAuthor(map, author), CommentDto.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#commentDto.author.id).name == authentication.name")
    public CommentDto send(CommentDto commentDto) {
        Long mapId = commentDto.getMap().getId();
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));

        Long userId = commentDto.getAuthor().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

        commentDto.setActive(true);

        Comment possibleExistingComment = commentRepository.findFirstByMapAndAuthor(map, user).orElse(null);
        if (possibleExistingComment != null) {
            possibleExistingComment.setValue(commentDto.getValue());
            return mapper.map(commentRepository.save(possibleExistingComment), CommentDto.class);
        } else {
            return mapper.map(commentRepository.save(mapper.map(commentDto, Comment.class)), CommentDto.class);
        }
    }

    @Override
    public Long getPagesCount(Long mapId, Long pageSize) {
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));
        Long mapCommentsCount = commentRepository.countAllByMap(map);
        boolean isFull = mapCommentsCount % pageSize == 0;
        return mapCommentsCount / pageSize + (isFull ? 0 : 1);
    }
}
