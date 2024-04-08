package net.elghz.siteservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Photo;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.categorie;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.export.ActivitieExcelExporter;
import net.elghz.siteservice.export.SiteExcelExporter;
import net.elghz.siteservice.importFile.ImporterSite;
//import net.elghz.siteservice.importFile.importerSite;
import net.elghz.siteservice.mapper.attributeMapper;
import net.elghz.siteservice.mapper.categorieMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.CategorieRepo;
import net.elghz.siteservice.repository.PhotoRepo;
import net.elghz.siteservice.repository.attributeRepo;
import net.elghz.siteservice.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import net.elghz.siteservice.service.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController

public class siteController {
    @Autowired  private siteService serv;
    @Autowired private categorieService cserv;
    @Autowired
    private ImporterSite importerSite;
@Autowired
    private attributeRepo arepo;
@Autowired
    private CategorieRepo catRepo;
@Autowired
    private siteMapper mapper;
@Autowired
    private attributeMapper amapper;
 @Autowired
 categorieMapper catmapper;

 @Autowired private CTService ctService;
 @Autowired private DCService dcService;
 @Autowired private PhotoRepo photoRepo;
    public siteController(siteService serv) {
        this.serv = serv;
    }

    @GetMapping("/sites")
    public ResponseEntity<?> allSites() {
        List<siteDTO> s = serv.allSites();
        if (!s.isEmpty()) {
            return new ResponseEntity<>(s, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune site n'est trouvé", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("site/type/{type}")
    public ResponseEntity<?> getSitesByType(@PathVariable String type) {
        try {
            SiteType siteType = SiteType.valueOfIgnoreCase(type.toUpperCase());
            Optional<List<siteDTO>> sites = serv.findByType(siteType);
            if (!sites.isEmpty()) {
                return new ResponseEntity<>(sites, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Aucun site trouvé pour le type: " + type, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Aucun site trouvé pour le type: " + type, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/site/id/{id}")
    public ResponseEntity<?> findSiteById(@PathVariable Long id) throws SiteNotFoundException {
        Optional<siteDTO> s = serv.findById(id);
        if (s.isPresent()) {
            return ResponseEntity.ok(s.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune site n'est trouvé avec ce ID");
        }

    }

    @DeleteMapping("site/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = serv.deleteById(id);
        if (deleted) {
            String uploadDir = "../site-images/" +id;
            FileUploadUtil.cleanDir(uploadDir);
            return new ResponseEntity<>("Le site est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune site n'est trouvé avec ce id" + id, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/site/add")
    public ResponseEntity<?> saveSite(@RequestBody siteDTO siteDTO) {
        try {
            return serv.saveSite(siteDTO);
        } catch (Exception e) {
            // Gérer l'erreur et retourner une réponse appropriée en cas d'exception
            return new ResponseEntity<>("Une erreur s'est produite lors de la sauvegarde du site.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/site/{id}")
    public ResponseEntity<String> updateSite(@PathVariable Long id, @RequestBody siteDTO updatedSite) {
        updatedSite.setId(id);
        boolean updated = serv.updateSite(updatedSite);
        if (updated) {
            return new ResponseEntity<>("Le site est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun site n'est trouvé avec ce Id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{sideId}/activities")
    public ResponseEntity<Set<typeActiviteDTO>> getSitesByTypeActivite(@PathVariable("sideId") Long s) {
        Set<typeActiviteDTO> sites = serv.getActivitiesBySite(s);
        return ResponseEntity.ok().body(sites);
    }

    @GetMapping("/site/{name}/equipements")
    public ResponseEntity<?> getAllEquipementSite(@PathVariable String name) {

        try {
            List<equipementDTO> equipements = serv.equipements(name);
            return new ResponseEntity<>(equipements, HttpStatus.OK);
        } catch (SiteNotFoundException ex) {
            return new ResponseEntity<>("Acune site n'est trouvé avec ce nom :" + name, HttpStatus.NOT_FOUND);
        } catch (EquipementNotFoundException ex) {
            return new ResponseEntity<>("Acune equipement n'est rouvé dans ce site :" + name, HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("add/equipement/site/{id}")
    public ResponseEntity<?> ajouterEquipementToSite(@RequestBody equipementDTO dto, @PathVariable Long id) {
        boolean b = serv.addEquipementToSite(id, dto);
        if (b) {
            return new ResponseEntity<>("L'équipement est bien ajouté au site", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune site n'est trouve avec ce id " + id, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{siteId}/equipements/{equipementId}")
    public ResponseEntity<?> removeEquipementFromSite(@PathVariable Long siteId, @PathVariable Long equipementId) {
        boolean removed = serv.removeEquipementFromSite(siteId, equipementId);
        if (removed) {
            return ResponseEntity.ok("L'équipement a été supprimé du site avec succès.");
        } else {

            boolean siteExists = serv.checkSiteExists(siteId);
            if (!siteExists) {
                return new ResponseEntity<>("Le site avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
            }
            boolean equipementExists = serv.checkEquipementExists(equipementId);
            if (!equipementExists) {
                return new ResponseEntity<>("L'équipement avec l'ID spécifié n'existe pas.", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'équipement spécifié n'est pas associé à ce site.");
        }
    }

    @GetMapping("/sites/export/excel")
    public void exportToExcel(HttpServletResponse servletResponse) throws IOException {
        List<siteDTO> siteDTOS = serv.allSites();
        SiteExcelExporter exporter = new SiteExcelExporter(ctService, dcService);
        exporter.export(siteDTOS, servletResponse);
    }

    @PostMapping("/import-sites")
    public ResponseEntity<?> importSites(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Veuillez sélectionner un fichier.", HttpStatus.BAD_REQUEST);
        }

        try {
            importerSite.importSites(file.getInputStream());
            return new ResponseEntity<>("Les sites ont été importés avec succès.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Une erreur s'est produite lors de l'importation des sites.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException ex) {
            String errorMessage = "Un site avec le même nom existe déjà dans la base de données.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }


    @PostMapping("/site/{siteId}/add-attribute/{attributeId}")
    public ResponseEntity<?> addAttributeToSite(@PathVariable Long siteId, @PathVariable Long attributeId) {
        return serv.addAttributeToSite(siteId, attributeId);
    }

    @PostMapping("/site/{siteId}/remove-attribute/{attributeId}")
    public ResponseEntity<?> removeAttributeFromSite(@PathVariable Long siteId, @PathVariable Long attributeId) {
        return serv.removeAttributeFromSite(siteId, attributeId);
    }

    @GetMapping("/site/{siteId}/attributes")
    public ResponseEntity<?> getAttributesOfSite(@PathVariable Long siteId) {
        return serv.getAttributesOfSite(siteId);
    }

    @PostMapping("/{ctId}/assign-site/{siteId}")
    public ResponseEntity<String> assignCentreToSite(@PathVariable Long ctId, @PathVariable Long siteId) throws NotFoundException {
        String message = serv.assignCentreTechniqueToSite(ctId, siteId);
        return ResponseEntity.ok().body(message);
    }
    @PostMapping("/{ctId}/unassign-site/{siteId}")
    public ResponseEntity<String> unassignSiteFromCT(@PathVariable Long ctId, @PathVariable Long siteId) {
        try {
            serv.unassignSiteFromCT(ctId, siteId);
            return ResponseEntity.ok("Le site a été désassocié du centre technique avec succès.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/CT/{ctId}/sites")
    public  ResponseEntity<?> getSitesByCT(@PathVariable Long ctId) throws NotFoundException {
        return serv.getSitesByCT(ctId);
    }

    @GetMapping("/CT/site/{id}")
    public ResponseEntity<?> getCTBySite(@PathVariable Long id ){
        return serv.getCTSite(id);

    }

    @PostMapping("/add/picts/site/{id}")
    public String savePicSit(@RequestParam("imagesSite") MultipartFile[] images,  @PathVariable Long id) throws IOException {
        siteDTO s = serv.findById(id).get();
        Site site = mapper.from(s);
        setExtraImagesName(images, site);
        saveUplodedImages(images, site);
         return "Les photos sont bien ajoutés au site: "+site.getName();
    }

    private void saveUplodedImages(MultipartFile[] images, Site site) throws IOException {
      if(images.length>0){
          String uploadDir = "site-service/site-images/" +site.getId();
          for(MultipartFile multipartFile: images){
              if(multipartFile.isEmpty())continue;
              String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
              FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
          }
      }
    }

    private void setExtraImagesName(MultipartFile[] images, Site site) {
        if(images.length>0){
            for (MultipartFile multipartFile: images){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    Photo p = new Photo(fileName, site);
                    p.setName(fileName);
                    site.addPhoto(p);
                    photoRepo.save(p);

                }
            }
        }
    }


}
