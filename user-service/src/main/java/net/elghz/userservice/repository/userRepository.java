package net.elghz.userservice.repository;
import net.elghz.userservice.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<utilisateur,Long> {

    utilisateur findByUsername(String username);
    boolean existsByUsername(String username);

}
