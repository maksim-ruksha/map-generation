package by.maksimruksha.mapgeneration.api.service;

import by.maksimruksha.mapgeneration.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto create(UserDto map);
    UserDto read(Long id);
    boolean update(UserDto map);
    boolean delete(Long id);
    Page<UserDto> findAll(Pageable pageable);
}
