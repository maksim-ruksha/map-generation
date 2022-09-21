package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //@Query("SELECT comment FROM Comment comment WHERE comment.map.id =: mapId")
    Page<Comment> findAllByMap(Long mapId, Pageable pageable);
}
