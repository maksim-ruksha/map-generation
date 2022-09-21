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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MapRepository mapRepository;

    private final ModelMapper mapper;

    @Override
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
    public CommentDto read(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment " + id + " not found."));
        return mapper.map(comment, CommentDto.class);
    }

    @Override
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentRepository.save(mapper.map(commentDto, Comment.class));
        return mapper.map(comment, CommentDto.class);
    }

    @Override
    public boolean delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<CommentDto> findAllByMap(Pageable pageable, Long mapId) {
        return commentRepository
                .findAllByMap(mapId, pageable)
                .map(comment -> mapper.map(comment, CommentDto.class));
    }
}
