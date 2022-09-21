package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
}
