package by.maksimruksha.mapgeneration.security.dto;

import by.maksimruksha.mapgeneration.dto.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDto implements Serializable {
    private final Long id;
    private final String name;
    private final List<UserDto> users;
}