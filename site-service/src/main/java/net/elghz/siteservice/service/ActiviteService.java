
package net.elghz.siteservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.importFile.importTypeActivite;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.mapper.typeActiviteMapper;
import net.elghz.siteservice.repository.ActiviteRepo;
import net.elghz.siteservice.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ActiviteService {

    private ActiviteRepo repo;
    private SiteRepository srepo;

    private typeActiviteMapper amapper;
    private siteMapper smapper;

    public Optional<typeActiviteDTO> getActiviteId(Long id) throws ActiviteNotFoundException {
        Optional<TypeActivite>  eq = repo.findById(id);
        if(eq.isPresent()) {
            typeActiviteDTO equipementDTO = amapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new ActiviteNotFoundException("Aucune activite avec ce id :" +id);
        }

    }

    public List<typeActiviteDTO> allActivities(){
        return repo.findAll().stream().map(amapper::from).collect(Collectors.toList());
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


    public boolean addTypeActivite(typeActiviteDTO a){
        String name = a.getName();
        Optional<TypeActivite> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            TypeActivite al = amapper.f(a);
            repo.save(al);
            return true;}
    }

    public boolean updateActivite(typeActiviteDTO updatedAct) {
        Long equiId = updatedAct.getId();
        Optional<TypeActivite> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            TypeActivite dtoe= existingAttrOptional.get();
            amapper.update(updatedAct , dtoe );

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }
    // trouver les activites d'un site donne

    public List<typeActiviteDTO> activitesSite(String name) throws SiteNotFoundException {

        Optional<Site> s = srepo.findByName(name);
        if (s.isPresent()){
            Long id = s.get().getId();
            return repo.findBySitesId(id).stream().map(amapper::from).collect(Collectors.toList());
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
    public Set<siteDTO> getSitesByTypeActivite(Long typeActiviteId) {
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);

        if (typeActiviteOptional.isPresent()) {
            TypeActivite typeActivite = typeActiviteOptional.get();

            // Convertir chaque Site en SiteDTO
            Set<siteDTO> siteDTOs = typeActivite.getSites().stream()
                    .map(site -> smapper.from(site))
                    .collect(Collectors.toSet());

            return siteDTOs;
        } else {
            throw new EntityNotFoundException("TypeActivite not found");
        }
    }

    //

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
    public Set<siteDTO> getSitesByMultipleTypeActivites(List<Long> typeActiviteIds) {
        Map<Long, siteDTO> siteMap = new HashMap<>();

        for (Long typeId : typeActiviteIds) {
            Optional<TypeActivite> typeActiviteOptional = repo.findById(typeId);
            typeActiviteOptional.ifPresent(typeActivite -> {
                Set<siteDTO> siteDTOs = typeActivite.getSites().stream()
                        .map(smapper::from)
                        .collect(Collectors.toSet());
                siteDTOs.forEach(site -> siteMap.put(site.getId(), site));
            });
        }
        return new HashSet<>(siteMap.values());
    }

    public boolean clearTypeActiviteForSite(Long siteId) {
        Optional<Site> siteOptional = srepo.findById(siteId);
        if (siteOptional.isPresent()) {
            Site site = siteOptional.get();
            site.clearTypeActivites();
            srepo.save(site);
            return  true;
        } else {
            //throw new EntityNotFoundException("le Site n'existe pas");
            return false;
        }
    }

    public boolean clearSitesForTypeActivite(Long typeActiviteId) {
        Optional<TypeActivite> typeActiviteOptional = repo.findById(typeActiviteId);
        if (typeActiviteOptional.isPresent()) {
            TypeActivite typeActivite = typeActiviteOptional.get();

            // Dissocier l'activité de tous les sites
            typeActivite.getSites().forEach(site -> site.removeTypeActivite(typeActivite));
            typeActivite.clearSites();

            // Enregistrer les changements
            repo.save(typeActivite);
            return true;
        } else {
            return  false;
        }
    }

    public void saveCustomersToDatabase(MultipartFile file){
        if(importTypeActivite.isValidExcelFile(file)){
            try {
                List<TypeActivite> customers = importTypeActivite.getCustomersDataFromExcel(file.getInputStream());
                this.repo.saveAll(customers);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

}
