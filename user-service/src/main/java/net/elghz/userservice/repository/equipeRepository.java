package net.elghz.userservice.repository;

import net.elghz.userservice.entities.EquipeIntervenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface equipeRepository extends JpaRepository<EquipeIntervenant, Long> {
}
