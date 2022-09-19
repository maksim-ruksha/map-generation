package by.maksimruksha.mapgeneration.dto;

import lombok.Data;

@Data
public class LikeDto {
    private Long id;
    private UserDto owner;
    private MapDto map;
}
