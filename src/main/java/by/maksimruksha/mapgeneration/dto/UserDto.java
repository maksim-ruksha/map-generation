package by.maksimruksha.mapgeneration.dto;

import by.maksimruksha.mapgeneration.security.entities.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String password;
    private Set<Role> roles;
    private List<MapDto> maps;
}
