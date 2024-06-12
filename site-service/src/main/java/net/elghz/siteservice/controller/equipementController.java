package net.elghz.siteservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.Statut;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.export.SiteExcelExporter;
import net.elghz.siteservice.export.exportEquipement;
import net.elghz.siteservice.importFile.ImporterSite;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.repository.PhotoEquiRepo;
import net.elghz.siteservice.repository.equipementRepo;
import net.elghz.siteservice.repository.typeEquipementRepo;
import net.elghz.siteservice.service.equipementService;
import net.elghz.siteservice.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sites")
public class equipementController {
    private equipementService serv;

    @Autowired
    equipementRepo repo;
    @Autowired
    PhotoEquiRepo photoRepo;
    @Autowired private equipementMapper mapper;
    @Autowired
    ImporterSite importerSite;
    @Autowired
    typeEquipementRepo equirepo;
    public equipementController (equipementService serv){
        this.serv = serv;
    }

    @GetMapping("/equipement/{id}")
    public equipementDTO getEquiById(@PathVariable Long id) {
        try {
            return serv.getEquipId(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun équipement avec cet ID : " + id));
        } catch (EquipementNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun équipement avec cet ID : " + id);
        }
    }



    @GetMapping("/getByName/{name}")
    public Long getIdByName(@PathVariable String name){
         return equirepo.findByName(name).get().getId();
    }
    @GetMapping("equi/by/{id}")
    public  equipementDTO getEquipementById(@PathVariable Long id){
         equipement e= repo.findById(id).get();
         equipementDTO dto= mapper.fromEquipement(e);
         return  dto;
    }

    @GetMapping("/equipement/all")
    public List<equipementDTO> getAllEquipements() {
        List<equipementDTO> equipements = serv.allEquipements();
        return equipements;
    }

    @GetMapping("/typeEqui/all")
        public List<typeEquipementDTO> getAllType() {

        return serv.getTypeEqui();
    }


    @PostMapping("/equipement/add/{idType}/{idS}")
    public equipementDTO addEquipement(@RequestBody equipementDTO equipementDTO , @PathVariable Long idType , @PathVariable Long idS) {

        return this.serv.addEquip(equipementDTO, idType , idS);
    }

    @PutMapping("/equipement/update")
    public ResponseEntity<String> updateEquipement(@RequestBody equipementDTO equipementDTO) {
        boolean updated = serv.updateEqui(equipementDTO);
        if (updated) {
            return new ResponseEntity<>("L'équipement a été mis à jour avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("L'équipement n'a pas été trouvé.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/equipement/delete/{id}")
    public void deleteEquipement(@PathVariable Long id) {
        boolean deleted = serv.deleteById(id);
        if (deleted) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "site-service/equipement-images/" +currentDate;
            FileUploadUtil.cleanDir(uploadDir);

        } else {

        }
    }


    @GetMapping("/getEquips/etat/{etat}")
    public List<equipementDTO> getEquiByEtat(@PathVariable Statut etat){
       List<equipement> e = repo.findByStatut(etat);
       return e.stream().map(mapper::from).collect(Collectors.toList());
    }
    @GetMapping ("equipement/etat/{name}")
        public Statut getEtat(@PathVariable String name){
        return  serv.etatEquipement(name);
        }



    @GetMapping("/exist/{numeroSérie}/{code}")
    public Boolean checkEquipExists( @PathVariable String numeroSérie, @PathVariable String code) {
         Optional<equipement> eq = repo.findByNumeroSerieOrCode( numeroSérie,code);
         if(eq.isPresent()){
              return true;
         }
         else {
              return  false;
         }
    }

    @GetMapping("/exist/{type}")
    public Boolean checkTypeEquiExist(  @PathVariable String type) {
        Optional<typeEquipement> eq = equirepo.findByName( type);
        if(eq.isPresent()){
            return true;
        }
        else {
            return  false;
        }
    }

    @PostMapping("/add/picts/equip/{id}")
    public String savePicEquip(@RequestParam("imagesEqui") MultipartFile[] images, @PathVariable Long id) throws IOException {


        equipementDTO dto= serv.getEquipId(id).get();
        equipement eq = mapper.from(dto);
        setExtraImagesName(images, eq);
        saveUplodedImages(images, eq);
        return "Les photos sont bien ajoutés à l'equipement: "+eq.getNom();
    }

    private void saveUplodedImages(MultipartFile[] images, equipement eq) throws IOException {
        if(images.length>0){
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "site-service/equipement-images/" +currentDate;
            for(MultipartFile multipartFile: images){
                if(multipartFile.isEmpty())continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    private void setExtraImagesName(MultipartFile[] images, equipement eq) {
        if(images.length>0){
            for (MultipartFile multipartFile: images){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    PhotoEquipement p = new PhotoEquipement(fileName, eq);
                    p.setName(fileName);
                    eq.addPhoto(p);
                    photoRepo.save(p);

                }
            }
        }
    }

    @GetMapping("/equip/id/{id}")
    public equipementDTO findEquipById(@PathVariable Long id){
        return serv.findById(id);
    }


    @PostMapping("/import-equip")
    public List<equipementDTO> importSitesParTypeActivite(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
           return  null;
        }

        try {
             return importerSite.importEquipements(file.getInputStream());

        } catch (ActiviteNotFoundException ex) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return  new ArrayList<>();
        }
    }

    @GetMapping("getLocalOfSite/{id}")
    public String getLocalisationOfSite(@PathVariable Long id){
         return serv.localisationOfEquip(id);
    }

    @GetMapping("nbre/equi")
    public int getNbreEquits(){
         return  repo.findAll().size();
    }


    @GetMapping("/All/type/equipements")
    public List<typeEquipementDTO> typeEquipents(){
         return  equirepo.findAll().stream().map(mapper::from).collect(Collectors.toList());
    }


    @PutMapping("/updatte/equipement/{id}/{idType}/{idSalle}")
    public equipementDTO updateEquipement(@PathVariable Long id, @RequestBody equipement updatedSite,@PathVariable Long idType, @PathVariable Long idSalle) {
        try {
            equipementDTO site = serv.updateEquipement(id, updatedSite,idType,idSalle);
            return site;
        } catch (RuntimeException e) {
            return null;
        }
    }

    @GetMapping("/existeEquipemnt/{code}/{id}")
    public Boolean checkifZquipementexiste(@PathVariable String code, @PathVariable Long id ){
        return repo.existsByCodeAndIdIsNot(code, id);
    }


//    @GetMapping("/findTypeEqui/{id}")
//    public typeEquipementDTO findTypeEquipemntById(@PathVariable Long id){
//
//        typeEquipement t= equirepo.findById(id).get();
//        return mapper.from(t);
//    }

    @PostMapping("/upload/picE/{idEq}")
    public List<PhotoEquipementDTO> uploadFilesEqui(@RequestParam("files") MultipartFile[] files, @PathVariable Long idEq) {
        try {
            equipement s = repo.findById(idEq).orElse(null);
            if (s == null) {
                return null;
            }
            List<PhotoEquipement> uploadedPhotos = new ArrayList<>();
            for (MultipartFile file : files) {
                PhotoEquipement fileEntity = new PhotoEquipement();
                fileEntity.setEquipement(s);
                s.getPhotoEquipements().add(fileEntity);
                fileEntity.setName(file.getOriginalFilename());
                fileEntity.setType(file.getContentType());
                fileEntity.setPicByte(file.getBytes());
                fileEntity.setDateAjout(new Date());
                photoRepo.save(fileEntity);
                uploadedPhotos.add(fileEntity);
            }
            String message = "Files uploaded successfully!";
            HttpStatus httpStatus = HttpStatus.CREATED;
            return uploadedPhotos.stream().map(mapper::from).collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping("/filesEq/{id}")
    public List<PhotoEquipementDTO> getFileEqui(@PathVariable Long id) {
        equipement s = repo.findById(id).get();
        List<PhotoEquipement> files = s.getPhotoEquipements();
        List<PhotoEquipementDTO> dtos= new ArrayList<>();
        for(PhotoEquipement p :files){
            dtos.add(mapper.from(p));
        }
        return dtos;
    }
    @DeleteMapping("/deletePicEquipement/{id}")
    public void deleteFile(@PathVariable Long id) {
        PhotoEquipement fileEntity = photoRepo.findById(id).orElse(null);
        if (fileEntity != null) {
            photoRepo.deleteById(id);

        }
    }


    @GetMapping("/equipements/export/excel")
    public void exportToExcel(HttpServletResponse servletResponse) throws IOException {
        List<equipement> siteDTOS = repo.findAll();
        exportEquipement exporter = new exportEquipement();
        exporter.exportEquipement(siteDTOS, servletResponse);
    }

    @GetMapping("/check/equipement/horsService/{id}")
    public  Boolean checkIfEquipementHorsService(@PathVariable Long id){
        return serv.checkIfEquipementIsHorsService(id);
    }

    @GetMapping ("/findEquipement/byKeyword/{keyword}")
    public List<equipementDTO> getEquipementsByKeyword(@PathVariable String  keyword){
        return repo.findequipentsByKeyword(keyword).stream().map(mapper::from).collect(Collectors.toList());
    }



//    @GetMapping("/find/byType/equipement/{name}")
//    public  Optional<typeEquipement> findByName(@PathVariable  String name){
//        return equirepo.findByName(name);
//    }
//    @PostMapping ("/add/type/equipement")
//    public typeEquipement addTypeEquipement(@RequestBody typeEquipement type){ return  equirepo.save(type);}


    @GetMapping("/findTypeEqui/{id}")
    public typeEquipementDTO findTypeEquipemntById(@PathVariable Long id){


        typeEquipement t = equirepo.findById(id).get();
        return  mapper.from(t);

    }

    @GetMapping("/find/byType/equipement/{name}")
    public Optional<typeEquipement> findByName(@PathVariable("name") String name){
         return equirepo.findByName(name);
    }

    @PostMapping("/add/type/equipement")
    public typeEquipement addTypeEquipement(@RequestBody typeEquipement type){

     return equirepo.save(type);

    }


    @GetMapping("/findTypeEquimeent/{id}")
    public typeEquipementDTO findTypeEquipemntByI(@PathVariable Long id){
         typeEquipement t = equirepo.findById(id).get();
         return  mapper.from(t);
    }

    @GetMapping("/getEquipementsoFtYpe/{id}")
    public List<equipementDTO> getEquiepemntOfType(@PathVariable Long id) {
        typeEquipement t= equirepo.findById(id).get();
        return t.getEquipements().stream().map(mapper::from).collect(Collectors.toList());
        }

    @GetMapping("/getVille/{id}")
    public String getVilleEqui(@PathVariable Long id){
        return  serv.siteEqui(id);
    }

    @GetMapping("/listEquipemnt/ofCnetre/{centre}")
    public  List<equipementDTO> getEquipOfCnetre(@PathVariable String centre){
        return serv.getEquipementsByCentreTechnique(centre);
    }

        @GetMapping("/getLocalisationOfEqui/{id}")
    public String getLocalisationOfEquip(@PathVariable Long id){
         return  serv.getLocalisationOfEquipemen(id);
        }
    }




