package net.elghz.siteservice.repository;

import net.elghz.siteservice.enumeration.SiteType;
import org.springframework.data.jpa.repository.JpaRepository;
import  net.elghz.siteservice.entities.*;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {

    //List<Site> findByType(SiteType type);
    List <Site> findByTypeactivitesId(Long Id);

    Optional<Site> findByName(String name);

    boolean existsByName(String siteName);


}
