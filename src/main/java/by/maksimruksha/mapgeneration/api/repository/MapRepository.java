package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Map;
import by.maksimruksha.mapgeneration.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, Long> {

    //@Query("SELECT map FROM Map map WHERE map.author.id =: userId")
    Page<Map> findAllByAuthor(Pageable pageable, User user);

    //Page<Map> findAll(Pageable pageable);
}
