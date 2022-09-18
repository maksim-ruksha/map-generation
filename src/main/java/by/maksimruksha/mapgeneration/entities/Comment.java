package by.maksimruksha.mapgeneration.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Author Id")
    private User author;

    @Column(name = "Value")
    private String value;
}
