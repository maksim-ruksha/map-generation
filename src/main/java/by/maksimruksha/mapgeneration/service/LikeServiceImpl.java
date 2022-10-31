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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final MapRepository mapRepository;

    private final ModelMapper mapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#likeDto.owner.id).name == authentication.name")
    public LikeDto create(LikeDto likeDto) {
        Long userId = likeDto.getOwner().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

        Long mapId = likeDto.getMap().getId();
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));
        if (!likeRepository.existsByMapAndOwner(map, user)) {
            Like like = mapper.map(likeDto, Like.class);

            like.setOwner(user);
            like.setMap(map);

            Like response = likeRepository.save(like);
            return mapper.map(response, LikeDto.class);
        } else {
            return null;
        }
    }

    @Override
    public LikeDto read(Long id) {
        Like like = likeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Like " + id + " not found."));
        return mapper.map(like, LikeDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#likeDto.owner.id).name == authentication.name")
    public LikeDto update(LikeDto likeDto) {
        Like like = likeRepository.save(mapper.map(likeDto, Like.class));
        return mapper.map(like, LikeDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#userId).name == authentication.name")
    public Boolean delete(Long userId, Long mapId) {
        if(userLikeExists(userId, mapId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));
            Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));

            Like like = likeRepository.findByMapAndOwner(map, user);
            likeRepository.delete(like);

            return true;
        }
        return false;
    }

    @Override
    public Boolean userLikeExists(Long userId, Long mapId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));
        Map map = mapRepository.findById(mapId).orElseThrow(() -> new ResourceNotFoundException("Map " + mapId + " not found."));

        return likeRepository.existsByMapAndOwner(map, user);
    }

    @Override
    public Page<LikeDto> findAll(Pageable pageable) {
        return likeRepository
                .findAll(pageable)
                .map(like -> mapper.map(like, LikeDto.class));
    }

    @Override
    public Page<LikeDto> findAllByMap(Pageable pageable, Long likeId) {
        return likeRepository
                .findAllByMap(pageable, likeId)
                .map(comment -> mapper.map(comment, LikeDto.class));
    }

    @Override
    public Long countAllByMap(Long mapId) {
        Map dummyMap = new Map();
        dummyMap.setId(mapId);
        return likeRepository.countAllByMap(dummyMap);
    }
}
