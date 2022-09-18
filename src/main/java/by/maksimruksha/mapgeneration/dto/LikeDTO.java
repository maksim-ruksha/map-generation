package by.maksimruksha.mapgeneration.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private UserDTO owner;
    private MapDTO map;
}
