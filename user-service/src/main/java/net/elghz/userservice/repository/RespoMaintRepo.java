package net.elghz.userservice.repository;

import net.elghz.userservice.entities.ResponsableMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RespoMaintRepo extends JpaRepository<ResponsableMaintenance , Long> {
   Optional<ResponsableMaintenance>  findByUsername(String username);
}
