package by.maksimruksha.mapgeneration.controller;

import by.maksimruksha.mapgeneration.api.service.UserService;
import by.maksimruksha.mapgeneration.dto.UserDto;
import by.maksimruksha.mapgeneration.entities.User;
import by.maksimruksha.mapgeneration.security.api.service.JwtService;
import by.maksimruksha.mapgeneration.security.dto.LoginDto;
import by.maksimruksha.mapgeneration.security.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    private final ModelMapper mapper;

    @GetMapping("/register")
    public ResponseEntity<UserDto> register(RegisterDto registerDto) {
        if (registerDto.getPassword().equals(registerDto.getPasswordRepeat())) {
            if (!userService.existsUserByName(registerDto.getName())) {
                UserDto userDto = mapper.map(registerDto, UserDto.class);
                UserDto response = userService.create(userDto);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/login")
    public ResponseEntity<String> login(LoginDto loginDto) {
        User user = mapper.map(loginDto, User.class);
        if (userService.existsUserByName(user.getName())) {
            if (!userService.checkPassword(user.getName(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password");
            }
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.notFound().build();
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

        User userDetails = mapper.map(loginDto, User.class);
        Boolean isValid = jwtService.isValid(token, userDetails);
        return isValid ? "Okay" : "Not okay";
    }
}
