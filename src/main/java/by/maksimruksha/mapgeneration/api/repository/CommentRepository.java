package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Comment;
import by.maksimruksha.mapgeneration.entities.Map;
import by.maksimruksha.mapgeneration.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByMap(Pageable pageable, Map map);
    Long countAllByMap(Map map);
    Boolean existsByMapAndAuthor(Map map, User author);
    Optional<Comment> findFirstByMapAndAuthor(Map map, User author);
}
