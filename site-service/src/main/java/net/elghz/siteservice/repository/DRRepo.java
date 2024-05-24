package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DRRepo extends JpaRepository<DR, Long> {
    Optional<DR> findByName(String dr);
    boolean existsByName(String name);
}
