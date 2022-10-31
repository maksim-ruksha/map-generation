package by.maksimruksha.mapgeneration.dto;

import by.maksimruksha.mapgeneration.entities.User;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MapDto {
    private Long id;
    private String seed;
    private String name;
    private String description;
    private UserDto author;
    private List<LikeDto> likes;
    private List<CommentDto> comments;
}
