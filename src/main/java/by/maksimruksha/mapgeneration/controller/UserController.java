package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.repository.UserRepository;
import by.maksimruksha.mapgeneration.dto.UserDto;
import by.maksimruksha.mapgeneration.entities.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public UserDto save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User response = userRepository.save(user);
        return modelMapper.map(response, UserDto.class);
    }
}
