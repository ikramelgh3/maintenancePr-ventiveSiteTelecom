package net.elghz.siteservice.repository;

import net.elghz.siteservice.enumeration.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;
import  net.elghz.siteservice.entities.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findByTypeSite(String type);
    List <Site> findByTypeactivitesId(Long Id);

    Optional<Site> findByName(String name);

    boolean existsByName(String siteName);

    @Query("SELECT c FROM Site  c where  c.name like %?1% OR c.centreTechnique.name like %?1% OR c.centreTechnique.dc.name like %?1% OR c.centreTechnique.dc.dr.name like %?1%")
    public List<Site> findSitesByKeyword(String keyword );

    Boolean existsByCodeAndIdIsNot(String code, Long id);

    boolean existsByCode(String name);

    Boolean existsByNameAndIdIsNot(String name, Long id);
}
