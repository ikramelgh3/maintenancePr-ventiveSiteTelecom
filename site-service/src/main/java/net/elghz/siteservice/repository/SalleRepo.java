package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.etage;
import net.elghz.siteservice.entities.salle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalleRepo extends JpaRepository<salle, Long> {
    Optional<salle> findByNumeroSalle(int dr);

    List<salle> findByEtage(etage dc);
}
