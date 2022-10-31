package by.maksimruksha.mapgeneration.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private UserDto author;
    private MapDto map;
    private String value;
    private Boolean active;
}
