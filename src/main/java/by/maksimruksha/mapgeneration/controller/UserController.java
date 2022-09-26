package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.UserService;
import by.maksimruksha.mapgeneration.dto.UserDto;
import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import by.maksimruksha.mapgeneration.security.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    private final ModelMapper mapper;

    @PostMapping("/create")
    public ResponseEntity<UserDto> save(UserDto userDto) {
        if (!userService.existsUserByName(userDto.getName())) {
            UserDto response = userService.create(userDto);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(userDto);
    }

    @GetMapping("/token")
    public String token() {
        LoginDto loginDto = new LoginDto();
        loginDto.setName("amogus");
        loginDto.setPassword("mega sus");

        UserDetails userDetails = mapper.map(loginDto, UserDetails.class);
        String token = jwtService.generateToken(userDetails);
        return token;
    }

    @GetMapping("/validateToken/{token}")
    public String validateToken(@PathVariable String token) {
        LoginDto loginDto = new LoginDto();
        loginDto.setName("amogus");
        loginDto.setPassword("mega sus");

        UserDetails userDetails = mapper.map(loginDto, UserDetails.class);
        Boolean isValid = jwtService.isValid(token, userDetails);
        return isValid ? "Okay" : "Not okay";
    }
}
