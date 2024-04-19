package net.elghz.interventionservice.repository;

import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface interventionRepo extends JpaRepository<Intervention , Long> {
    Optional<Intervention> findByName(String name);

    @Override
    List<Intervention> findAllById(Iterable<Long> longs);

    List<Intervention> findByStatus(statusIntervention status);
}
