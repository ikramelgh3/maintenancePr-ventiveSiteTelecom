package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DCRepo extends JpaRepository<DC, Long> {

    Optional<DC> findByName(String dr);

    List<DC> findByDr(DR dr);
    boolean existsByName(String name);


}
