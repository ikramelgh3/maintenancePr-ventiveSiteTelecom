package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EtageRepo extends JpaRepository<etage, Long> {
    Optional<etage> findByNumeroEtage(int dr);

    List<etage> findByImmuble(immuble dc);
    Optional <etage> findByCodeEtagge(String code);
}
