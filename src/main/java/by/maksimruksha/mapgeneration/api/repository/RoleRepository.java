package by.maksimruksha.mapgeneration.api.repository;

import by.maksimruksha.mapgeneration.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
