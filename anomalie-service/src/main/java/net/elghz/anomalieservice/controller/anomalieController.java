package net.elghz.anomalieservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.anomalieservice.dto.AnomalieDTO;
import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.entities.PhotoAnomalie;
import net.elghz.anomalieservice.enumeration.MomentPrisephoto;
import net.elghz.anomalieservice.enumeration.StatusGravite;
import net.elghz.anomalieservice.mapper.mapper;
import net.elghz.anomalieservice.model.ActionCorrective;
import net.elghz.anomalieservice.repository.PhotoAnomalieRepository;
import net.elghz.anomalieservice.service.anomalieService;
import net.elghz.anomalieservice.util.FileUploadUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor

public class anomalieController {

    private anomalieService ser;
    private mapper mp;
    private PhotoAnomalieRepository photorepo;

    @GetMapping("/get/all/anomalies")
        public ResponseEntity<?> getAllAnomalies(){
        List<AnomalieDTO>  anomalies = ser.getAllAnomalies();
        if(anomalies.size()==0){
            return new ResponseEntity<>("Aucune anomalie detectée" , HttpStatus.OK);
        }
        return new ResponseEntity<>(ser.getAllAnomalies() , HttpStatus.OK);
    }
    @PostMapping("/declarer/anomalie")
    public ResponseEntity<?> detecterAnomalie(@RequestBody AnomalieDTO anomalie ){
     return   ser.declarerAnomalie(anomalie);
    }

    @GetMapping("/anomalies/ouvert")
    public List<AnomalieDTO> anomalieList (){
        return ser.getAnomaliesOuvertes();
    }

    @GetMapping("/anomalies/en_retard")
    public List<AnomalieDTO> anomalieRetard (){
        return ser.getAnomaliesEnRetard();
    }

    @GetMapping("/anomalie/enreatrd/{idAnomalie}")
    public String verifierEtMettreAJourStatutEnRetard(@PathVariable Long idAnomalie) {
        ser.metterAjourStatus(idAnomalie);
        return "Statut de l'anomalie vérifié et mis à jour si nécessaire.";
    }

    @GetMapping("/get/anomalie/{id}")
    public AnomalieDTO getAnomalieById(@PathVariable Long id){
        return ser.getAnomalie(id);
    }

    @DeleteMapping("/delete/anomalie/ferme")
    public ResponseEntity<?> deleteAnomalie(@RequestParam Long id){
        if(ser.deleteAnomalie(id)) {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "anomalie-service/anomalie-images/Apres-resolution/" +currentDate;
            String uploadDirA = "anomalie-service/anomalie-images/Avant-resolution/ " +currentDate;
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.cleanDir(uploadDirA);
            return new ResponseEntity<>("L'anomalie est supprimé", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Vous ne pouvez pas supprimer cette anomalie tant qu'elle n'est pas fermée.", HttpStatus.OK);
        }
    }

    @PutMapping("/anomalies/updateStatus")
    public ResponseEntity<String> updateAnomalieStatus(@RequestParam Long id, @RequestParam StatusGravite newStatus) {
        boolean success = ser.updateAnomalieStatus(id, newStatus);
        if (success) {
            return ResponseEntity.ok("Le statut de l'anomalie a été modifié avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Anomalie introuvable.");
        }
    }
    @GetMapping("/anomlies/found/byTechnicien")
    public List<AnomalieDTO> getAnomliesFoundedByTech(@RequestParam Long id){
        return ser.getAnomalieDetecteByTech(id);
    }

    @GetMapping("/anomiles/DetectedIn/Interevntion")
    public List<AnomalieDTO> anomaliesDetectedInIntervention(@RequestParam  Long IdIntervention){
    return ser.anomaliesDetectedInInterevntion(IdIntervention);
    }


    @PostMapping("/add/picts/anomalie/AvantR")
    public String savePicsAnomalieAvantR(@RequestParam Long id,@RequestParam("imagesAnomalie") MultipartFile[] images) throws IOException {
        AnomalieDTO dto = ser.getAnomalie(id);
        Anomalie anomalie = mp.from(dto);
        setExtraImagesName(images, anomalie);
        saveUplodedImages(images, anomalie);
        return "Les photos sont bien ajoutés à l'anomalie: "+anomalie.getTitre();
    }

    @PostMapping("/add/picts/anomalie/ApresR")
    public String savePicsAnomalieApresR(@RequestParam Long id,@RequestParam("imagesAnomalie") MultipartFile[] images) throws IOException {
        AnomalieDTO dto = ser.getAnomalie(id);
        Anomalie anomalie = mp.from(dto);
        if(dto.getActionsCorrectives().size()==0){
            return "Vous devez d'abord effectuer les actions correctives. ";
        }
        setExtraImagesName(images, anomalie);
        saveUplodedImagesApres(images, anomalie);
        return "Les photos sont bien ajoutés à l'anomalie: "+anomalie.getTitre();
    }
    private void saveUplodedImages(MultipartFile[] images, Anomalie site) throws IOException {
        if(images.length>0){
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "anomalie-service/anomalie-images/Avant-resolution/" +currentDate;
            for(MultipartFile multipartFile: images){
                if(multipartFile.isEmpty())continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    private void saveUplodedImagesApres(MultipartFile[] images, Anomalie site) throws IOException {
        if(images.length>0){
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String uploadDir = "anomalie-service/anomalie-images/Apres-resolution/" +currentDate;
            for(MultipartFile multipartFile: images){
                if(multipartFile.isEmpty())continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }
    private void setExtraImagesName(MultipartFile[] images, Anomalie anomalie) {
        if(images.length>0){
            for (MultipartFile multipartFile: images){
                if(!multipartFile.isEmpty()){
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    if(anomalie.getStatus().equals(StatusGravite.OUVERTE)){
                        PhotoAnomalie p = new PhotoAnomalie(fileName, anomalie , MomentPrisephoto.AVANT_RESOLUTION);
                        p.setName(fileName);
                        anomalie.addPhoto(p);
                        photorepo.save(p);
                    }
                    if(anomalie.getStatus().equals(StatusGravite.FERMEE)){
                        PhotoAnomalie p = new PhotoAnomalie(fileName, anomalie , MomentPrisephoto.APRES_RESOLUTION);
                        p.setName(fileName);
                        anomalie.addPhoto(p);
                        photorepo.save(p);
                    }

                }
            }
        }
    }

    private void removeExtraImagesName( Anomalie site) {
        Set<PhotoAnomalie> images = site.getPhotos();
        if(site.getPhotos().size()>0){
            for (PhotoAnomalie multipartFile: images){
                site.removePhoto(multipartFile);
                multipartFile.setAnomalie(null);
            }
        }
    }

    @PutMapping("/attribuer/anomalie/technicien")
    public ResponseEntity<?> attribuerAnomalieToTechncien(@RequestParam Long idAn,@RequestParam Long idTech ){
        return ser.attribuerAnomalieToTechnicien(idAn, idTech);
    }

    @GetMapping("action")
    public List<ActionCorrective> actionsCorrectivesForAnomalie(@RequestParam Long id){
         return ser.actionsCorrectiveForAnomalie(id);
    }
}

