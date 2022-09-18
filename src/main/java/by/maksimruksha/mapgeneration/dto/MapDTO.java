package by.maksimruksha.mapgeneration.dto;

import by.maksimruksha.mapgeneration.entities.User;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MapDTO {
    private Long id;
    private ZonedDateTime publicationDateTime;
    private Long seed;
    private User author;
    private String generatorName;
    private List<LikeDTO> likes;
    private List<CommentDTO> comments;
    private String previewLink;
    private String fullLink;
}
