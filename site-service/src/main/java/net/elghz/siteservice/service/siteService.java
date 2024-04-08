package net.elghz.siteservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.mapper.typeActiviteMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import net.elghz.siteservice.repository.*;

import net.elghz.siteservice.entities.*;

import java.util.Iterator;
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

    private CategorieRepo catrepo;
    private attributeRepo attributeRepository;

    public Optional<List<siteDTO>> findByType(SiteType type) {
        List<Site> sites = repo.findByType(type);


        return Optional.ofNullable(sites.stream().map(smapper::from).collect(Collectors.toList()));

    }
    public List<siteDTO> allSites(){
        return repo.findAll().stream().map(smapper::from).collect(Collectors.toList());
    }



    public Optional <siteDTO> findById(Long id) throws SiteNotFoundException{

        Optional<Site>  eq = repo.findById(id);
        if(eq.isPresent()) {
            siteDTO equipementDTO = smapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new SiteNotFoundException("Aucune site avec ce id :" +id);
        }

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

    /*public ResponseEntity<?> saveSite(siteDTO site) {
        String siteName = site.getName();
        if (isSiteNameUnique(siteName)) {

            Site savedSite = repo.save(smapper.from(site));
            return new ResponseEntity<>(savedSite, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Le nom de site est déjà utilisé.", HttpStatus.BAD_REQUEST);
        }
    }
*/

    @Transactional
    public ResponseEntity<?> saveSite(siteDTO site) {
        String siteName = site.getName();

        if (!isSiteNameUnique(siteName)) {
            return new ResponseEntity<>("Le nom de site est déjà utilisé.", HttpStatus.BAD_REQUEST);
        }
        Site siteEntity = smapper.from(site);

        for (Attribute attribute : siteEntity.getAttributs()) {

            categorie categorie = attribute.getCategorie();

            entityManager.persist(categorie);
            entityManager.persist(attribute);
        }

        Site savedSite = repo.save(siteEntity);
        return new ResponseEntity<>(savedSite, HttpStatus.CREATED);
    }


    public boolean updateSite(siteDTO updatedSiteDTO) {
        Long siteId = updatedSiteDTO.getId();
        Optional<Site> existingSiteOptional = repo.findById(siteId);

        if (existingSiteOptional.isPresent()) {
            Site existingSite = existingSiteOptional.get();
           // smapper.update(updatedSiteDTO, existingSite);
            repo.save(existingSite);
            return true;
        } else {
            return false;
        }
    }

    //les activite d'un site donne
    public Set<typeActiviteDTO> getActivitiesBySite(Long s) {
        Optional<Site> siteOptional = repo.findById(s);

        if (siteOptional.isPresent()) {
            Site ss = siteOptional.get();
            Set<typeActiviteDTO> typeActiviteDTOs = ss.getTypeactivites().stream()
                    .map(amapper::from)
                    .collect(Collectors.toSet());
            return typeActiviteDTOs;
        } else {
            throw new EntityNotFoundException("Le site avec l'ID " + s + " n'existe pas.");
        }
    }

    //afficher les equipements d'un site donné

    public List<equipementDTO> equipements(String name)  throws  EquipementNotFoundException, SiteNotFoundException{

        Optional<Site> siteOptional = repo.findByName(name);
        if (siteOptional.isPresent()) {
            Site site = siteOptional.get();
            List<equipement> equipements = site.getEquipements();
            if (equipements.isEmpty()) {
                throw new EquipementNotFoundException("Audun équipement trouvé dans le site avec le nom: " + name);
            }
            List<equipementDTO> equipementDTOS = equipements.stream()
                    .map(equipement -> mapperEqui.fromEquipement(equipement) )
                    .collect(Collectors.toList());
            return equipementDTOS;
        } else {
            throw new SiteNotFoundException("Aucun site trouvé avec le nom: " + name);
        }
    }
    //ajouter un equipement à un site donnee
    public boolean addEquipementToSite(Long idIte , equipementDTO e){

        Optional<Site> s = repo.findById(idIte);
        if (s.isPresent()){
            equipement ee = mapperEqui.from(e);
            Site ss  = s.get();
            ee.setSite(ss);
            ss.addEquipement(ee);
            repo.save(ss);
            return  true;
        }
        else {
            return false;
        }
        }

    public boolean removeEquipementFromSite(Long siteId, Long equipementId) {
        Optional<Site> siteOptional = repo.findById(siteId);
        if (siteOptional.isPresent()) {
            Site site = siteOptional.get();
            Optional<equipement> equipementOptional = site.getEquipements().stream()
                    .filter(e -> e.getId().equals(equipementId))
                    .findFirst();
            if (equipementOptional.isPresent()) {
                equipement equipement = equipementOptional.get();
                site.removeEquipement(equipement);
                repo.save(site);
                return true;
            } else {
                return false;
            }
        } else {

            return false;
        }
    }
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
    }


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

        if( centreTechnique.getSites().isEmpty()){
            return new ResponseEntity<>("Aucune site n'est associe à ce centre technqiue", HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(centreTechnique.getSites(), HttpStatus.OK);
        }
    }


    public ResponseEntity<?> getCTSite(Long id){
        Optional<Site> s = repo.findById(id);
        if(s.isPresent()){
            siteDTO dto = smapper.from(s.get());
            Optional<CentreTechniqueDTO> ctDTO = Optional.ofNullable(dto.getCentreTechnique());
            if(ctDTO.isPresent()){
                return  new ResponseEntity<>("Le centre technique associe à ce site est : "+dto.getName() , HttpStatus.OK);
            }
            else {
                return  new ResponseEntity<>("Aucune Centre technique n'est associe à ce site " , HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>("Site non trouvé avec l'ID : "+ id , HttpStatus.NOT_FOUND);
        }
    }


}
