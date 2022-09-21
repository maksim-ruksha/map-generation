package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Page<Like> findAllByMap(Pageable pageable, Long mapId);
    Long countAllByMap(Long mapId);
}
