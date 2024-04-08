package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface equipementRepo  extends JpaRepository<equipement,Long> {

    Optional<equipement> findByNomOrNumeroSerie(String dr , String num);
    Optional<equipement> findByNom(String dr);
}

