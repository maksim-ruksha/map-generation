package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.repository.CommentRepository;
import by.maksimruksha.mapgeneration.api.repository.LikeRepository;
import by.maksimruksha.mapgeneration.api.repository.MapRepository;
import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.api.service.MapService;
import by.maksimruksha.mapgeneration.dto.MapDto;
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
public class MapServiceImpl implements MapService {

    private final MapRepository mapRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final ModelMapper mapper;

    @Override
    public MapDto create(MapDto mapDto) {
        Long userId = mapDto.getAuthor().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));

        Map map = mapper.map(mapDto, Map.class);
        map.setAuthor(user);

        Map response = mapRepository.save(map);
        return mapper.map(response, MapDto.class);
    }

    @Override
    public MapDto read(Long id) {
        Map comment = mapRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Map " + id + " not found."));
        return mapper.map(comment, MapDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#mapDto.author.id).name == authentication.name")
    public MapDto update(MapDto mapDto) {
        Map map = mapRepository.save(mapper.map(mapDto, Map.class));
        return mapper.map(map, MapDto.class);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN') || @userServiceImpl.read(#mapDto.author.id).name == authentication.name")
    public Boolean delete(MapDto mapDto) {
        if (mapRepository.existsById(mapDto.getId())) {
            mapRepository.deleteById(mapDto.getId());
            return true;
        }
        return false;
    }

    @Override
    public Page<MapDto> findAll(Pageable pageable) {
        return mapRepository.findAll(pageable).map(map -> mapper.map(map, MapDto.class));
    }

    @Override
    public Page<MapDto> findAllByAuthor(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));
        return mapRepository
                .findAllByAuthor(pageable, user)
                .map(map -> mapper.map(map, MapDto.class));
    }

    @Override
    public Long getPagesCount(Long pageSize) {
        Long mapsCount = mapRepository.count();
        Boolean isFull = mapsCount % pageSize == 0;
        return mapsCount / pageSize + (isFull ? 0 : 1);
    }
}
