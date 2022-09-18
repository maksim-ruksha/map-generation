package by.maksimruksha.mapgeneration.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Like {

    @Id
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Owner Id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Map Id")
    private Map map;
}
