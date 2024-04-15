package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.immuble;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImmubleRepo extends JpaRepository<immuble, Long> {
    Optional<immuble> findByName(String dr);

    List<immuble> findBySite(Site site);
}
