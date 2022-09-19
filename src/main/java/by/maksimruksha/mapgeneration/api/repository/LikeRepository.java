package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    /*@Query("SELECT like FROM Like like WHERE like.map.id =: mapId")
    List<Like> getMapLikes(Long mapId);

    @Query("SELECT COUNT(Like.id) FROM Like like WHERE like.map.id =: mapId")
    Long getMapLikeCount(Long mapId);*/

    Page<Like> findAllByMap(Pageable pageable, Long mapId);
}
