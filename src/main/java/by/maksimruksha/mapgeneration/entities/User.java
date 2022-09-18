package by.maksimruksha.mapgeneration.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User {
    @Id
    private Long id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Maps Ids")
    private List<Map> maps;
}
