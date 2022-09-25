package by.maksimruksha.mapgeneration.service;

import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.api.service.UserService;
import by.maksimruksha.mapgeneration.dto.UserDto;
import by.maksimruksha.mapgeneration.entities.User;
import by.maksimruksha.mapgeneration.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User response = userRepository.save(mapper.map(userDto, User.class));
        return mapper.map(response, UserDto.class);
    }

    @Override
    public UserDto read(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.save(mapper.map(userDto, User.class));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public Boolean delete(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository
                .findAll(pageable)
                .map(user -> mapper.map(user, UserDto.class));
    }

    @Override
    public UserDto findById(Long userId) {
        User response = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found."));
        return mapper.map(response, UserDto.class);
    }

    @Override
    public UserDto findByName(String name) {
        User response = userRepository.findUserByName(name);
        return mapper.map(response, UserDto.class);
    }

    @Override
    public Boolean existsUserByName(String name) {
        return userRepository.existsUserByName(name);
    }
}
