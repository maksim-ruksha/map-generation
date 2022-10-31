package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Like;
import by.maksimruksha.mapgeneration.entities.Map;
import by.maksimruksha.mapgeneration.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LikeRepository extends JpaRepository<Like, Long> {
    Page<Like> findAllByMap(Pageable pageable, Long mapId);
    Boolean existsByMapAndOwner(Map map, User owner);
    Like findByMapAndOwner(Map map, User owner);
    Long countAllByMap(Map map);
}
