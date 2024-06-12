package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import net.elghz.siteservice.enumeration.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface equipementRepo  extends JpaRepository<equipement,Long> {

    Optional<equipement> findByNumeroSerieOrCode( String num , String code);
    Optional<equipement> findByNom(String dr);

    Optional<equipement> findByNumeroSerie(String numSerie);

    List<equipement> findByStatut(Statut status);
    Optional<equipement> findByCode(String code);
    boolean existsByCodeAndIdIsNot(String code, Long id);
    boolean existsByNumeroSerieAndIdIsNot(String num, Long id);
    @Query("SELECT c FROM equipement  c where  c.nom like %?1% OR c.code like %?1% OR c.numeroSerie like %?1% OR c.marque like %?1% OR c.salle.codeSalle like  %?1% or c.salle.etage.codeEtagge like %?1% or c.salle.etage.immuble.name like  %?1% or c.salle.etage.immuble.site.name like %?1% or c.typeEquipementt.name like  %?1%")
    public List<equipement> findequipentsByKeyword(String keyword );
}