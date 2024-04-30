package net.elghz.siteservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import net.elghz.siteservice.repository.*;

import net.elghz.siteservice.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class siteService {

    private SiteRepository repo;
    private ActiviteRepo arepo;
    private equipementMapper mapperEqui;
    private siteMapper smapper;
    private typeActiviteMapper amapper;
    @PersistenceContext
    private EntityManager entityManager;
    private CTRepo ctRepo;

    @Autowired
    private siteMobileMapper siteMobileMap;
    @Autowired
    private siteFixeMapper siteFixeMap;
    private CategorieRepo catrepo;
    private attributeRepo attributeRepository;


    public List<siteDTO> sitesByType(String type){
        return repo.findByTypeSite(type).stream().map(smapper::from).collect(Collectors.toList());
    }

    public List<siteDTO> allSites() {
        return repo.findAll().stream().map(smapper::from).collect(Collectors.toList());
    }


    public Optional<siteDTO> findById(Long id) throws SiteNotFoundException {

        Optional<Site> eq = repo.findById(id);
        if (eq.isPresent()) {
            siteDTO equipementDTO = smapper.from(eq.get());
            return Optional.of(equipementDTO);
        } else {
            throw new SiteNotFoundException("Aucune site avec ce id :" + id);
        }

    }

    public ResponseEntity<?> findSiteById(Long id) throws SiteNotFoundException {
        Optional<Site> optionalSite = repo.findById(id);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            if (site instanceof SiteMobile) {
                SiteMobile sm = (SiteMobile) site;
                SiteMobileDTO dto = smapper.fromMobile(sm);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else if (site instanceof SiteFixe) {
                SiteFixe sf = (SiteFixe) site;
                SiteFixeDTO dto = smapper.fromFixe(sf);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                // Gérer d'autres types de site si nécessaire
                return new ResponseEntity<>("Type de site non pris en charge", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new SiteNotFoundException("Aucun site trouvé avec l'ID: " + id);
        }
    }

    public siteDTO getSite(Long id) {
        Optional<Site> optionalSite = repo.findById(id);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            if (site instanceof SiteMobile) {
                SiteMobile sm = (SiteMobile) site;
                return smapper.fromMobile(sm);
            } else if (site instanceof SiteFixe) {
                SiteFixe sf = (SiteFixe) site;
                return smapper.fromFixe(sf);
            }
        }
        throw new RuntimeException("Site not found with ID: " + id);
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

    public boolean checkSiteExists(Long siteId) {
        return repo.existsById(siteId);
    }

    public boolean checkEquipementExists(Long equipementId) {
        return repo.existsById(equipementId);
    }


    public ResponseEntity<?> saveSiteMobile(SiteMobile site) {
        String siteName = site.getName();

        if (!isSiteNameUnique(siteName)) {
            return new ResponseEntity<>("Le nom de site est déjà utilisé.", HttpStatus.BAD_REQUEST);
        } else {
//            Site s = smapper.from(site);
            site.setTypeSite("Mobile");
            //siteDTO dto =smapper.mapToSiteDTO(site);
           // Site s = smapper.from(dto);
            repo.save(site);
            return new ResponseEntity<>(site, HttpStatus.CREATED);
        }

    }

    public ResponseEntity<?> saveSiteFixe (SiteFixe site) {
        String siteName = site.getName();

        if (!isSiteNameUnique(siteName)) {
            return new ResponseEntity<>("Le nom de site est déjà utilisé.", HttpStatus.BAD_REQUEST);
        } else {
            site.setTypeSite("Fixe");
            repo.save(site);
            return new ResponseEntity<>(site, HttpStatus.CREATED);
        }

    }




    public ResponseEntity<?> addSite(Site site) {
        if (site instanceof SiteFixe) {
            // Traitement spécifique pour les sites fixes
            return new ResponseEntity<>(site, HttpStatus.CREATED);
        } else if (site instanceof SiteMobile) {
            // Traitement spécifique pour les sites mobiles
            return new ResponseEntity<>(site, HttpStatus.CREATED);
        } else {
            throw new IllegalArgumentException("Type de site non pris en charge");
        }
    }



    public  boolean updateSiteFixe(SiteFixeDTO updatedSiteDTO){

        Long siteId = updatedSiteDTO.getId();
        Optional<Site> existingSiteOptional = repo.findById(siteId);

        if (existingSiteOptional.isPresent()) {
            Site existingSite = existingSiteOptional.get();
            siteFixeMap.update(updatedSiteDTO, existingSite);
            repo.save(existingSite);
            return true;
        } else {
            return false;
        }
    }


    public boolean updateSite(siteDTO updatedSiteDTO) {
        Long siteId = updatedSiteDTO.getId();
        Optional<Site> existingSiteOptional = repo.findById(siteId);

        if (existingSiteOptional.isPresent()) {
            Site existingSite = existingSiteOptional.get();
            existingSite.update(updatedSiteDTO);
            repo.save(existingSite);
            return true;
        } else {
            return false; // Aucun site trouvé avec l'ID donné
        }
    }
    //les activite d'un site donne
    public  ResponseEntity<?>getActivitiesBySite(Long s) {
        Optional<Site> siteOptional = repo.findById(s);

        if (siteOptional.isPresent()) {
            Site ss = siteOptional.get();
            Set<typeActiviteDTO> typeActiviteDTOs = ss.getTypeactivites().stream()
                    .map(amapper::from)
                    .collect(Collectors.toSet());
            return  new ResponseEntity<>(typeActiviteDTOs, HttpStatus.OK );
        } else {
            return  new ResponseEntity<>("Le site avec l'ID " + s + " n'existe pas.", HttpStatus.NOT_FOUND);
        }
    }

    //afficher les equipements d'un site donné

    /*
        public void associerCategorieAuSite(Long siteId, Long categorieId) {
            Site site = repo.findById(siteId).orElseThrow(() -> new EntityNotFoundException("Site not found"));
            categorie categorie = catrepo.findById(categorieId).orElseThrow(() -> new EntityNotFoundException("Category not found"));

            site.addCategorie(categorie);
            repo.save(site);
        }

        public void dissocierCategorieDuSite(Long siteId, Long categorieId) {
            Site site = repo.findById(siteId).orElseThrow(() -> new EntityNotFoundException("Site not found"));
            categorie categorie = catrepo.findById(categorieId).orElseThrow(() -> new EntityNotFoundException("Category not found"));

            site.removeCategori(categorie);
            repo.save(site);
        }
    */
    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }


/*
    public ResponseEntity<?> addAttributeToSite(Long siteId, Long attributeId) {
        Optional<Site> optionalSite = repo.findById(siteId);
        Optional<Attribute> optionalAttribute = attributeRepository.findById(attributeId);

        if (optionalSite.isPresent() && optionalAttribute.isPresent()) {
            Site site = optionalSite.get();
            Attribute attribute = optionalAttribute.get();

            // Vérifier si l'attribut existe déjà dans la liste des attributs du site
            if (!site.getAttributs().contains(attribute)) {
                site.addAttribute(attribute);
                repo.save(site);
                return new ResponseEntity<>("L'attribut a été associé au site avec succès.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("L'attribut est déjà associé à ce site.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Le site ou l'attribut n'existe pas.", HttpStatus.NOT_FOUND);
        }
    }
*/
/*
    public ResponseEntity<?> removeAttributeFromSite(Long siteId, Long attributeId) {
        Optional<Site> optionalSite = repo.findById(siteId);
        Optional<Attribute> optionalAttribute = attributeRepository.findById(attributeId);

        if (optionalSite.isPresent() && optionalAttribute.isPresent()) {
            Site site = optionalSite.get();
            Attribute attribute = optionalAttribute.get();

            site.removeAttribute(attribute);
            repo.save(site);

            return new ResponseEntity<>("L'attribut a été dissocié du site avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le site ou l'attribut n'existe pas.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getAttributesOfSite(Long siteId) {
        Optional<Site> optionalSite = repo.findById(siteId);

        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            siteDTO dto = smapper.from(site);
            Set<attributeDTO> attributes = dto.getAttributs();

            return new ResponseEntity<>(attributes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Le site n'existe pas.", HttpStatus.NOT_FOUND);
        }
    }*/


    public String assignCentreTechniqueToSite(Long centreTechniqueId, Long siteId) {
        Optional<CentreTechnique> centreTechniqueOptional = ctRepo.findById(centreTechniqueId);
        Optional<Site> siteOptional = repo.findById(siteId);

        if (centreTechniqueOptional.isEmpty() || siteOptional.isEmpty()) {
            return "Le centre technique ou le site n'a pas été trouvé.";
        }

        CentreTechnique centreTechnique = centreTechniqueOptional.get();
        Site site = siteOptional.get();

        if (site.getCentreTechnique() != null) {
            return "Le site est déjà associé au centre technique : " + site.getCentreTechnique().getName();
        }

        site.setCentreTechnique(centreTechnique);
        repo.save(site);

        return "Le centre technique a été associé au site avec succès.";
    }

    public void unassignSiteFromCT(Long ctId, Long siteId) throws NotFoundException {
        CentreTechnique centreTechnique = ctRepo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Centre technique non trouvé avec l'ID : " + ctId));

        Site site = repo.findById(siteId)
                .orElseThrow(() -> new NotFoundException("Site non trouvé avec l'ID : " + siteId));

        if (site.getCentreTechnique() != null) {
            centreTechnique.getSites().remove(site);
            site.setCentreTechnique(null);
            repo.save(site);
        } else {
            throw new NotFoundException("Le site n'est associé à aucun centre technique.");
        }
    }

    public ResponseEntity<?> getSitesByCT(Long ctId) throws NotFoundException {
        CentreTechnique centreTechnique = ctRepo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Centre technique non trouvé avec l'ID : " + ctId));

        if (centreTechnique.getSites().isEmpty()) {
            return new ResponseEntity<>("Aucune site n'est associe à ce centre technqiue", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(centreTechnique.getSites(), HttpStatus.OK);
        }
    }


    public ResponseEntity<?> getCTSite(Long id) {
        Optional<Site> s = repo.findById(id);
        if (s.isPresent()) {
            siteDTO dto = smapper.from(s.get());
            Optional<CentreTechniqueDTO> ctDTO = Optional.ofNullable(dto.getCentreTechnique());
            if (ctDTO.isPresent()) {
                return new ResponseEntity<>("Le centre technique associe à ce site est : " + dto.getName(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Aucune Centre technique n'est associe à ce site ", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("Site non trouvé avec l'ID : " + id, HttpStatus.NOT_FOUND);
        }
    }







}
