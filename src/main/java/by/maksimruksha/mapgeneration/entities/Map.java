package by.maksimruksha.mapgeneration.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "maps")
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seed")
    private String seed;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "likes_ids")
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "comments_ids")
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "maps_hashtags", joinColumns = {
            @JoinColumn(name = "map_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "hashtag_id", referencedColumnName = "id")
            })
    private List<Hashtag> hashtags;
}
