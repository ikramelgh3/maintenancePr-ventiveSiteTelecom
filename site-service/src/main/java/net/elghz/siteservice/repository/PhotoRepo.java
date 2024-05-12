package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.Photo;
import net.elghz.siteservice.entities.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepo extends JpaRepository<Photo, Long> {
    Optional<Photo> findByName(String imageName);

    List<Photo> findBySite(Site site);
}
