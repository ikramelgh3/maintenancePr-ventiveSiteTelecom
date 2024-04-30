package net.elghz.siteservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.export.SiteExcelExporter;
import net.elghz.siteservice.importFile.ImporterSite;
//import net.elghz.siteservice.importFile.importerSite;
import net.elghz.siteservice.mapper.attributeMapper;
import net.elghz.siteservice.mapper.categorieMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.CategorieRepo;
import net.elghz.siteservice.repository.PhotoRepo;
import net.elghz.siteservice.repository.SiteRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;

@RestController

public class siteController {
    @Autowired  private siteService serv;
    @Autowired private categorieService cserv;
    @Autowired
    private ImporterSite importerSite;
    @Autowired
    SiteRepository repo;
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
    public List<siteDTO> allSites() {
        return serv.allSites();

    }

    @GetMapping("get/sites/type/{type}")
    public List<siteDTO> getSitesByType(@PathVariable String type){
        return serv.sitesByType(type);
    }
/*
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
    }*/
/*
    @GetMapping("/site/id/{id}")
    public ResponseEntity<?> findSiteById(@PathVariable Long id) throws SiteNotFoundException {
       return serv.findSiteById(id);

    }*/

    @GetMapping("/site/id/{id}")
    public siteDTO findType(@PathVariable Long id) throws SiteNotFoundException {
        return serv.getSite(id);

    }

    @DeleteMapping("site/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = serv.deleteById(id);
        if (deleted) {
          //  removeExtraImagesName(repo.findById(id).get());

            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "site-service/site-images/" +currentDate;
            FileUploadUtil.cleanDir(uploadDir);
            return new ResponseEntity<>("Le site est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune site n'est trouvé avec ce id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/siteMobile/add")
    public ResponseEntity <?> addSite(@RequestBody SiteMobile mb){

        return  serv.saveSiteMobile(mb);
    }

    @PostMapping("/siteFixe/add")
    public ResponseEntity <?> addSite(@RequestBody SiteFixe mb){

        return  serv.saveSiteFixe(mb);
    }



    //afficher les sites par type
    @GetMapping("/{sideId}/activities")
    public ResponseEntity<?> getSitesByTypeActivite(@PathVariable("sideId") Long s) {
        return  serv.getActivitiesBySite(s);

    }




    @GetMapping("/sites/export/excel")
    public void exportToExcel(HttpServletResponse servletResponse) throws IOException {
        List<Site> siteDTOS = repo.findAll();
        SiteExcelExporter exporter = new SiteExcelExporter(ctService, dcService);
        exporter.export(siteDTOS, servletResponse);
    }

    @PostMapping("/import-sites")
    public ResponseEntity<?> importSites(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Veuillez sélectionner un fichier.", HttpStatus.BAD_REQUEST);
        }

        try {
            importerSite.importSites(file.getInputStream() );
            return new ResponseEntity<>("Les sites ont été importés avec succès.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Une erreur s'est produite lors de l'importation des sites.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException ex) {
            String errorMessage = "Un site avec le même nom existe déjà dans la base de données.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }

/*
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
*/
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
          String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
          String uploadDir = "site-service/site-images/" +currentDate;
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


    private void removeExtraImagesName( Site site) {
        List<Photo> images = site.getPhotos();
        if(site.getPhotos().size()>0){

            for (Photo multipartFile: images){

                   site.removePhoto(multipartFile);
                   multipartFile.setSite(null);


                }
            }
        }








}
