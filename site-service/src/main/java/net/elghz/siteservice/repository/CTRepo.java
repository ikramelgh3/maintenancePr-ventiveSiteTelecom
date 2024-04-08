package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.TypeActivite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CTRepo extends JpaRepository<CentreTechnique, Long> {
    Optional<CentreTechnique> findByName(String dr);

    List<CentreTechnique> findByDc(DC dc);
}
