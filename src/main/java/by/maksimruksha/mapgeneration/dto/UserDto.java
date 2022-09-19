package by.maksimruksha.mapgeneration.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    // private String password;
    private String role;
    private List<MapDto> maps;
}
