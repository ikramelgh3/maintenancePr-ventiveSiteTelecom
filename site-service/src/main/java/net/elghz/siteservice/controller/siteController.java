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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import net.elghz.siteservice.service.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @PostMapping("/ajouter/site/mobile/{idC}")
    public SiteMobileDTO addStMobile(@RequestBody SiteMobile s, @PathVariable Long idC){
         return  serv.ajouterSiteMobile(s, idC);
    }

    @PostMapping("/ajouter/site/fixe/{idC}")
    public SiteFixeDTO addStFixe(@RequestBody SiteFixe s, @PathVariable Long idC){
        return  serv.ajouterSiteFixe(s, idC);
    }
    @GetMapping("get/sites/type/{type}")
    public List<siteDTO> getSitesByType(@PathVariable String type){
        return serv.sitesByType(type);
    }


    @GetMapping("/site/id/{id}")
    public siteDTO findType(@PathVariable Long id) throws SiteNotFoundException {
        return serv.getSite(id);

    }

    @DeleteMapping("site/delete/{id}")
    public void delete(@PathVariable Long id) {
        serv.deleteById(id);
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
    public List<siteDTO> importSites(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }


        try {
          return   importerSite.importSites(file.getInputStream() );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();

        } catch (DataIntegrityViolationException ex) {
            return new ArrayList<>();

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
    @GetMapping("/getPath/{idSite}")
    public List<String> getImagePath(@PathVariable Long idSite){
        Site s = repo.findById(idSite).get();
        return s.getPhotosImagePaths();
    }

    private void saveUplodedImages(MultipartFile[] images, Site site) throws IOException {
      if(images.length>0){
          String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
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


    private void removeExtraImagesName( Site site) {
        List<Photo> images = site.getPhotos();
        if(site.getPhotos().size()>0){

            for (Photo multipartFile: images){

                   site.removePhoto(multipartFile);
                   multipartFile.setSite(null);


                }
            }
        }



        @GetMapping("/find/site/name/{name}")
        public siteDTO getSiteByNamr(@PathVariable String name){
         return serv.findByNamr(name);
        }



        @GetMapping("/equipements/ofSITE/{id}")
    public  List<equipementDTO> getEquiOfSite(@PathVariable Long id){
        return serv.getEquipementsOfSite(id);
        }

        @GetMapping("/get/immubles/site/{id}")
    public List<immubleDTO> getImmubleOfSite(@PathVariable Long id){
        return serv.getImmublesOfSite(id);
        }

        @GetMapping("/get/size/sites")
    public int getNombreSites(){
         return  serv.allSites().size();
        }

        @GetMapping("/get/photos/{id}")
        public List<PhotoDTO> getPhotosSite(@PathVariable Long id){
         return  serv.getPhotosOfSite(id);
        }

         @GetMapping("/get/imagePath/{id}")
    public List<String> imagesPathSite(@PathVariable Long id){
         return serv.getCheminImagesSite(id);
         }



    @PostMapping("/upload/pic/{idSite}")
    public List<PhotoDTO> uploadFiles(@RequestParam("files") MultipartFile[] files, @PathVariable Long idSite) {
        try {
            Site s = repo.findById(idSite).orElse(null);
            if (s == null) {
                return null;
            }
            List<Photo> uploadedPhotos = new ArrayList<>();
            for (MultipartFile file : files) {
                Photo fileEntity = new Photo();
                fileEntity.setSite(s);
                s.getPhotos().add(fileEntity);
                fileEntity.setName(file.getOriginalFilename());
                fileEntity.setType(file.getContentType());
                fileEntity.setPicByte(file.getBytes());
                fileEntity.setDateAjout(new Date());
                photoRepo.save(fileEntity);
                uploadedPhotos.add(fileEntity);
            }

            String message = "Files uploaded successfully!";
            HttpStatus httpStatus = HttpStatus.CREATED;
            // Vous pouvez retourner les photos téléchargées ou un message de succès ici
            return uploadedPhotos.stream().map(mapper::from).collect(Collectors.toList());
        } catch (IOException e) {
            // Gérer les exceptions d'entrée/sortie ici
            return null;
        }
    }


    @PutMapping("/update/pic/{idSite}")
    public List<PhotoDTO> updateFiles(@RequestParam("files") MultipartFile[] files, @PathVariable Long idSite) {
        try {
            Site site = repo.findById(idSite).orElse(null);
            if (site == null) {
                return null;
            }

            List<Photo> existingPhotos = photoRepo.findBySite(site);
            List<Photo> updatedPhotos = new ArrayList<>();

            // Parcourir les nouveaux fichiers
            for (MultipartFile file : files) {
                // Vérifier si le fichier existe déjà
                Optional<Photo> existingPhotoOptional = existingPhotos.stream()
                        .filter(photo -> photo.getName().equals(file.getOriginalFilename()))
                        .findFirst();

                if (existingPhotoOptional.isPresent()) {
                    // Si le fichier existe déjà, mettre à jour son contenu
                    Photo existingPhoto = existingPhotoOptional.get();
                    existingPhoto.setType(file.getContentType());
                    existingPhoto.setPicByte(file.getBytes());
                    existingPhoto.setDateAjout(new Date());
                    photoRepo.save(existingPhoto);
                    updatedPhotos.add(existingPhoto);
                } else {
                    // Si le fichier n'existe pas, ajouter une nouvelle photo
                    Photo newPhoto = new Photo();
                    newPhoto.setSite(site);
                    site.getPhotos().add(newPhoto);
                    newPhoto.setName(file.getOriginalFilename());
                    newPhoto.setType(file.getContentType());
                    newPhoto.setPicByte(file.getBytes());
                    newPhoto.setDateAjout(new Date());
                    photoRepo.save(newPhoto);
                    updatedPhotos.add(newPhoto);
                }
            }

            String message = "Files updated successfully!";
            HttpStatus httpStatus = HttpStatus.OK;
            // Vous pouvez retourner les photos mises à jour ou un message de succès ici
            return updatedPhotos.stream().map(mapper::from).collect(Collectors.toList());
        } catch (IOException e) {
            // Gérer les exceptions d'entrée/sortie ici
            return null;
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        Photo fileEntity = photoRepo.findById(id).orElse(null);
        if (fileEntity != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileEntity.getType()));
            headers.setContentDisposition(ContentDisposition.attachment().filename(fileEntity.getName()).build());
            ByteArrayResource resource = new ByteArrayResource(fileEntity.getPicByte());
            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/files/{id}")
    public List<PhotoDTO> getFile(@PathVariable Long id) {
        Site s = repo.findById(id).get();
        List<Photo> files = s.getPhotos();
        List<PhotoDTO> dtos= new ArrayList<>();
        for(Photo p :files){
            dtos.add(mapper.from(p));
        }
        return dtos;
    }




    @DeleteMapping("/deletePic/{id}")
    public String deleteFile(@PathVariable Long id) {
        Photo fileEntity = photoRepo.findById(id).orElse(null);
        if (fileEntity != null) {
           photoRepo.deleteById(id);
            return "La photo est supprime";
        } else {
            return "Aucune photo avec ce id";
        }
    }


    @GetMapping("/exists/{name}")
    public Boolean checkSiteExists(@PathVariable String name) {
        boolean exists = repo.existsByName(name);
        return exists;
    }
    @GetMapping("/existsbyCode/{code}")
    public Boolean checkSiteExistsCode(@PathVariable String code) {
        boolean exists = repo.existsByCode(code);
        return exists;
    }


    @PutMapping("/updatte/siteFixe/{id}")
    public SiteFixeDTO updateSiteFixe(@PathVariable Long id, @RequestBody SiteFixe updatedSite) {
        try {
            SiteFixeDTO site = serv.updateSiteFixe(id, updatedSite);
            return site;
        } catch (RuntimeException e) {
            return null;
        }
    }
    @PutMapping("/update/siteMobile/{id}")
    public SiteMobileDTO updateSiteMobile(@PathVariable Long id, @RequestBody SiteMobile updatedSite) {
        try {
            SiteMobileDTO site = serv.updateSiteMobile(id, updatedSite);
            return site;
        } catch (RuntimeException e) {
            return null;
        }
    }


    @GetMapping("/getImageOfSite/{id}")
    public  List<PhotoDTO> getPhotos(@PathVariable Long id){
         Site s = repo.findById(id).get();
         return  s.getPhotos().stream().map(mapper::from).collect(Collectors.toList());
    }

    @DeleteMapping("delete/pic/{id}")
    public void deletePicSite(@PathVariable Long id){
         Photo p = photoRepo.findById(id).get();
         photoRepo.deleteById(id);

    }
    @GetMapping("/getTotal")
    public  int getTot(){
         return  repo.findAll().size();
    }

    @GetMapping ("/findSite/byKeyword/{keyword}")
    public List<siteDTO> getSitesByKeyword(@PathVariable String  keyword){
         return repo.findSitesByKeyword(keyword).stream().map(mapper::from).collect(Collectors.toList());
    }

    @GetMapping("/existeSite/{code}/{id}")
    public Boolean checkifSiteexiste(@PathVariable String code, @PathVariable Long id ){
        return repo.existsByCodeAndIdIsNot(code, id);
    }

    @GetMapping("/existeSiteName/{name}/{id}")
    public Boolean checkifSiteexisteByName(@PathVariable String name, @PathVariable Long id ){
        return repo.existsByNameAndIdIsNot(name, id);
    }

    @GetMapping("/getImmubles/ofSite/{id}")
    public List<immubleDTO> getImmublesOfSite(@PathVariable Long id){
         return  repo.findById(id).get().getImmubles().stream().map(mapper::from).collect(Collectors.toList());
    }

    @GetMapping("/getSalle/ofSite/{id}")
    public List<salleDTO> getSalleOfSite(@PathVariable Long id){
         Site s = repo.findById(id).get();
         List<salle> sl= new ArrayList<>();
         List<immuble> immubleDTOS = s.getImmubles();
         for(immuble im:immubleDTOS){
             List<etage> et=  im.getEtageList();
             for(etage e :et){
                  sl= e.getSalles();
             }
         }
         return  sl.stream().map(mapper::from).collect(Collectors.toList());
    }

}
