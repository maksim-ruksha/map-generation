package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
