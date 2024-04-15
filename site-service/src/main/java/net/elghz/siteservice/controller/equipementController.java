package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.entities.Photo;
import net.elghz.siteservice.entities.PhotoEquipement;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.equipement;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.repository.PhotoEquiRepo;
import net.elghz.siteservice.repository.equipementRepo;
import net.elghz.siteservice.service.equipementService;
import net.elghz.siteservice.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class equipementController {
    private equipementService serv;

    @Autowired
    equipementRepo repo;
    @Autowired
    PhotoEquiRepo photoRepo;
    @Autowired private equipementMapper mapper;
    public equipementController (equipementService serv){
        this.serv = serv;
    }

    @GetMapping("/equipement/{id}")
    public ResponseEntity<?> getEquiById(@PathVariable Long id) {
        try {
            Optional<equipementDTO> dtoOptional = serv.getEquipId(id);
            return new ResponseEntity<>(dtoOptional.orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun équipement avec cet ID : " + id)),
                    HttpStatus.OK);
        } catch (EquipementNotFoundException ex) {
            return new ResponseEntity<>("Aucun équipement avec cet ID : " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/equipement/all")
    public ResponseEntity<List<equipementDTO>> getAllEquipements() {
        List<equipementDTO> equipements = serv.allEquipements();
        return new ResponseEntity<>(equipements, HttpStatus.OK);
    }

    @PostMapping("/equipement/add")
    public ResponseEntity<String> addEquipement(@RequestBody equipementDTO equipementDTO) {
        boolean added = serv.addEquip(equipementDTO);
        if (added) {
            return new ResponseEntity<>("L'équipement a été ajouté avec succès.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("L'équipement existe déjà.", HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<String> deleteEquipement(@PathVariable Long id) {
        boolean deleted = serv.deleteById(id);
        if (deleted) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "site-service/equipement-images/" +currentDate;
            FileUploadUtil.cleanDir(uploadDir);
            return new ResponseEntity<>("L'équipement a été supprimé avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("L'équipement n'a pas été trouvé.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping ("equipement/etat/{name}")
        public String getEtat(@PathVariable String name){
        return  serv.etatEquipement(name);
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

}
