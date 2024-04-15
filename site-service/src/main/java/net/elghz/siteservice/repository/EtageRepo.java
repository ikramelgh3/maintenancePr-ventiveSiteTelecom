package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.etage;
import net.elghz.siteservice.entities.immuble;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtageRepo extends JpaRepository<etage, Long> {
    Optional<etage> findByNumeroEtage(int dr);

    List<etage> findByImmuble(immuble dc);
}
