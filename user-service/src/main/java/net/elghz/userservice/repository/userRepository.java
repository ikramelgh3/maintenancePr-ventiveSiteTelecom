package net.elghz.userservice.repository;
import net.elghz.userservice.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface userRepository extends JpaRepository<utilisateur,Long> {

    utilisateur findByUsername(String username);
    boolean existsByUsername(String username);

    List<utilisateur> findByRoles_RoleName(String technicien);
}
