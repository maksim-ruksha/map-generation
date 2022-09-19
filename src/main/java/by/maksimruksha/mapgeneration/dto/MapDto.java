package by.maksimruksha.mapgeneration.dto;

import by.maksimruksha.mapgeneration.entities.User;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MapDto {
    private Long id;
    private ZonedDateTime publicationDateTime;
    private Long seed;
    private User author;
    private String generatorName;
    private List<LikeDto> likes;
    private List<CommentDto> comments;
    private String previewLink;
    private String fullLink;
}
