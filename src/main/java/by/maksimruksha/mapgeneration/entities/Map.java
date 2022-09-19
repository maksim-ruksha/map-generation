package by.maksimruksha.mapgeneration.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Maps")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Publication Date Time")
    private ZonedDateTime publicationDateTime;

    @Column(name = "Seed")
    private Long seed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Author Id")
    private User author;

    @Column(name = "Generator")
    private String generatorName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Likes Ids")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Comments Ids")
    private List<Comment> comments;

    @Column(name = "Preview Link")
    private String previewLink;

    @Column(name = "Full Link")
    private String fullLink;
}
