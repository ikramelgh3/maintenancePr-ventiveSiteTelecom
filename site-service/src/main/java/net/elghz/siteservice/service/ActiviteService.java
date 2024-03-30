package net.elghz.siteservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.repository.ActiviteRepo;
import net.elghz.siteservice.repository.SiteRepository;
import net.elghz.siteservice.repository.attributeRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class ActiviteService {

    private ActiviteRepo repo;
    private SiteRepository srepo;


    public Optional<TypeActivite> getActiviteId(Long id){
        return  repo.findById(id);
    }

    public List<TypeActivite> allActivities(){
        return repo.findAll();
    }

    public boolean deleteById(Long id) {
        Optional<TypeActivite> ActivOptional = repo.findById(id);
        if (ActivOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public boolean addTypeActivite(TypeActivite a){
        String name = a.getName();
        Optional<TypeActivite> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            repo.save(a);
            return true;}
    }

    public boolean updateActivite(TypeActivite updatedAct) {
        Long attributeId = updatedAct.getId();
        Optional<TypeActivite> existingActvOptional = repo.findById(attributeId);

        if (existingActvOptional.isPresent()) {
            TypeActivite existingActv = existingActvOptional.get();
            existingActv.setName(updatedAct.getName());

            repo.save(existingActv);

            return true;
        } else {
            return false;
        }
    }
    // trouver les activites d'un site donne

    public List<TypeActivite> activitesSite(String name) throws SiteNotFoundException {

        Optional<Site> s = srepo.findByName(name);
        if (s.isPresent()){
            Long id = s.get().getId();
            return repo.findBySitesId(id);
        }
        else {
            throw  new SiteNotFoundException("Aucune site n'est trouvé avec ce nom" +name);
        }

    }

    //manipulation des sites par type d'activité
    //ajouter un site à un type d'activite
    public void addSiteToTypeActivite(Long siteId, Long typeActiviteId) {
        Optional<Site> siteOptional = srepo.findById(siteId);
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);

        if (siteOptional.isPresent() && typeActiviteOptional.isPresent()) {
            Site site = siteOptional.get();
            TypeActivite typeActivite = typeActiviteOptional.get();
            typeActivite.addSite(site);

            repo.save(typeActivite);
        } else {
            throw new EntityNotFoundException("le site ou le type d'activité est non trouvé!!!");
        }
    }

    //supprimer un site d'une activite donne


    public void removeSiteFromTypeActivite(Long siteId, Long typeActiviteId) {
        Optional<Site> siteOptional = srepo.findById(siteId);
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);

        if (siteOptional.isPresent() && typeActiviteOptional.isPresent()) {
            Site site = siteOptional.get();
            TypeActivite typeActivite = typeActiviteOptional.get();
            typeActivite.removeSite(site);

            repo.save(typeActivite);
        } else {
            throw new EntityNotFoundException("le site ou le type d'activité est non trouvé!!!");
        }
    }

    // la liste des sites d'une activite donne
    public Set<Site> getSitesByTypeActivite(Long typeActiviteId) {
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);

        if (typeActiviteOptional.isPresent()) {
            TypeActivite typeActivite = typeActiviteOptional.get();
            return typeActivite.getSites();
        } else {
            throw new EntityNotFoundException("TypeActivite not found");
        }
    }

    //mettre à jour les type d'activite associe à un site
    public void updateSiteTypeActivite(Long siteId, List<Long> typeActiviteIds) {
        Optional<Site> siteOptional = srepo.findById(siteId);
        if (siteOptional.isPresent()) {
            Site site = siteOptional.get();
            site.clearTypeActivites();

            for (Long typeId : typeActiviteIds) {
                Optional<TypeActivite> typeActiviteOptional = repo.findById(typeId);
                typeActiviteOptional.ifPresent(site::addTypeActivite);
            }

            srepo.save(site);
        } else {
            throw new EntityNotFoundException("Le site n'existe pas");
        }
    }

    //afficher les sites qu'ont plusieurs type d'activites
    public Set<Site> getSitesByMultipleTypeActivites(List<Long> typeActiviteIds) {
        Set<Site> sites = new HashSet<>();
        for (Long typeId : typeActiviteIds) {
            Optional<TypeActivite> typeActiviteOptional = repo.findById(typeId);
            typeActiviteOptional.ifPresent(typeActivite -> sites.addAll(typeActivite.getSites()));
        }
        return sites;
    }

    public void clearTypeActiviteForSite(Long siteId) {
        Optional<Site> siteOptional = srepo.findById(siteId);
        if (siteOptional.isPresent()) {
            Site site = siteOptional.get();
            site.clearTypeActivites();
            srepo.save(site);
        } else {
            throw new EntityNotFoundException("le Site n'existe pas");
        }
    }

    public void clearSitesForTypeActivite(Long typeActiviteId) {
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);
        if (typeActiviteOptional.isPresent()) {
            TypeActivite typeActivite = typeActiviteOptional.get();
            typeActivite.clearSites();
            repo.save(typeActivite);
        } else {
            throw new EntityNotFoundException(" le TypeActivite n'existe pas");
        }
    }

}
