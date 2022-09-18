package by.maksimruksha.mapgeneration.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private UserDTO author;
    private MapDTO map;
    private String value;
}
