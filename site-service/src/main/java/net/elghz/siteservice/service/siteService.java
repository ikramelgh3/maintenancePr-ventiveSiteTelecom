package net.elghz.siteservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import net.elghz.siteservice.repository.*;

import net.elghz.siteservice.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class siteService {

    private SiteRepository repo;
    private ActiviteRepo arepo;


    public Optional<List<Site>> findByType(SiteType type) {
        List<Site> sites = repo.findByType(type);
        return Optional.ofNullable(sites);
    }
    public List<Site> allSites(){
        return repo.findAll();
    }

    public Optional<Site> findById(Long id){
        return repo.findById(id);
    }

    public boolean deleteById(Long id) {
        Optional<Site> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    private boolean isSiteNameUnique(String siteName) {
        Optional<Site> existingSite = repo.findByName(siteName);
        return existingSite.isEmpty();
    }


    public ResponseEntity<?> saveSite(Site site) {
        String siteName = site.getName();
        if (isSiteNameUnique(siteName)) {
            Site savedSite = repo.save(site);
            return new ResponseEntity<>(savedSite, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Le nom de site est déjà utilisé.", HttpStatus.BAD_REQUEST);
        }
    }





    public boolean updateSite(Site updatedSite) {
        Long attributeId = updatedSite.getId();
        Optional<Site> existingSiteOptional = repo.findById(attributeId);

        if (existingSiteOptional.isPresent()) {
            Site existingActv = existingSiteOptional.get();
            existingActv.setName(updatedSite.getName());

            repo.save(existingActv);

            return true;
        } else {
            return false;
        }
    }

    //les activite d'un site donne
    public Set<TypeActivite> getActivitiesBySite(Long s) {
        Optional<Site> siteOptional = repo.findById(s);

        if (siteOptional.isPresent()) {
            Site ss = siteOptional.get();
            return ss.getTypeactivites();
        } else {
            throw new EntityNotFoundException("le site n'existe pas ");
        }
    }

}