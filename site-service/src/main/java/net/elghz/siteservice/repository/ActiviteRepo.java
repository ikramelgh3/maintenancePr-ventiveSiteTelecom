package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActiviteRepo extends JpaRepository <TypeActivite, Long> {
   Optional<TypeActivite>  findByName(String dr);

   List<TypeActivite> findBySitesId(Long id);

}
