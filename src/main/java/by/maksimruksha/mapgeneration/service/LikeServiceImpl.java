package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.repository.LikeRepository;
import by.maksimruksha.mapgeneration.api.repository.MapRepository;
import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.api.service.LikeService;
import by.maksimruksha.mapgeneration.dto.LikeDto;
import by.maksimruksha.mapgeneration.entities.Like;
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
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final MapRepository mapRepository;

    private final ModelMapper mapper;

    @Override
    public LikeDto create(LikeDto commentDto) {
        Long userId = commentDto.getOwner().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

        Long mapId = commentDto.getMap().getId();
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));

        Like comment = mapper.map(commentDto, Like.class);

        comment.setOwner(user);
        comment.setMap(map);

        Like response = likeRepository.save(comment);
        return mapper.map(response, LikeDto.class);
    }

    @Override
    public LikeDto read(Long id) {
        Like comment = likeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Like " + id + " not found."));
        return mapper.map(comment, LikeDto.class);
    }

    @Override
    public LikeDto update(LikeDto commentDto) {
        Like comment = likeRepository.save(mapper.map(commentDto, Like.class));
        return mapper.map(comment, LikeDto.class);
    }

    @Override
    public boolean delete(Long id) {
        if (likeRepository.existsById(id)) {
            likeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Page<LikeDto> findAll(Pageable pageable) {
        return likeRepository
                .findAll(pageable)
                .map(like -> mapper.map(like, LikeDto.class));
    }

    @Override
    public Page<LikeDto> findAllByMap(Pageable pageable, Long mapId) {
        return likeRepository
                .findAllByMap(pageable, mapId)
                .map(comment -> mapper.map(comment, LikeDto.class));
    }

    @Override
    public Long countAllByMap(Long mapId) {
        return likeRepository.countAllByMap(mapId);
    }
}
