package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto create(UserDto userDto);
    UserDto read(Long userId);
    UserDto update(UserDto userDto);
    boolean delete(Long userId);
    Page<UserDto> findAll(Pageable pageable);
    UserDto findById(Long userId);
}
