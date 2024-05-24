package net.elghz.siteservice.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.export.ActivitieCSVExporter;
import net.elghz.siteservice.export.ActivitieExcelExporter;
import net.elghz.siteservice.export.ActivitiePDFExporter;
import net.elghz.siteservice.importFile.ImporterSite;
import net.elghz.siteservice.service.ActiviteService;
import net.elghz.siteservice.service.attributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController

public class activiteController {

    private ActiviteService ser;

    @Autowired
    private ImporterSite importerSite;
    public activiteController(ActiviteService ser) {
        this.ser = ser;
    }

    @GetMapping("/activite/id/{id}")
    public ResponseEntity<?> getActiviteId(@PathVariable Long id) throws ActiviteNotFoundException {
        Optional<typeActiviteDTO> a = ser.getActiviteId(id);
        if(a.isPresent()){
            return new ResponseEntity<>(a , HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("Aucune activité n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/activite/all")
    public ResponseEntity<?> getActivities(){
        List<typeActiviteDTO> activities = ser.allActivities();
        if(!activities.isEmpty()){
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("La liste des activités est vide", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/activite/add")
    public ResponseEntity<String> addActivite(@RequestBody typeActiviteDTO ac) {
        boolean added = ser.addTypeActivite(ac);
        if (added) {
            return new ResponseEntity<>("L'activté est bien ajouté", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("l'activite avec ce nom est déja existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/activite/{id}")
    public ResponseEntity<String> updateActivite(@PathVariable Long id, @RequestBody typeActiviteDTO updatedActv) {
        updatedActv.setId(id);
        boolean updated = ser.updateActivite(updatedActv);
        if (updated) {
            return new ResponseEntity<>("L'activite est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun activite n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/activite/{id}")
    public ResponseEntity<String> deleteActivite(@PathVariable Long id) {
        boolean deleted = ser.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>("L'activite est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun activite n'est trouvée avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/activites/site/{name}")
    public ResponseEntity<?> activitiesSite(@PathVariable String name){

        try {
            List <typeActiviteDTO> s =ser.activitesSite(name);
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (SiteNotFoundException e) {
            return  new ResponseEntity<>(" Ce site n'a pas d'activités", HttpStatus.NOT_FOUND);
        }

    }

    //associe un site à une activite
    @PostMapping("/{typeActiviteId}/sites/{siteId}")
    public ResponseEntity<String> addSiteToTypeActivite(@PathVariable Long siteId, @PathVariable Long typeActiviteId) throws ActiviteNotFoundException {
        ser.addSiteToTypeActivite(siteId, typeActiviteId);
        Optional<typeActiviteDTO> a = ser.getActiviteId(typeActiviteId);
        String m = a.get().getName();
        return ResponseEntity.ok("Le site est bien ajouté à l'activité: "+m);
    }

    @DeleteMapping("/{typeActiviteId}/sites/{siteId}")
    public ResponseEntity<String> RemoveSiteFromTypeActivite(@PathVariable Long siteId, @PathVariable Long typeActiviteId) throws ActiviteNotFoundException {
        ser.removeSiteFromTypeActivite(siteId, typeActiviteId);
        Optional<typeActiviteDTO> a = ser.getActiviteId(typeActiviteId);
        String m = a.get().getName();
        return ResponseEntity.ok("Le site est bien supprimé de l'activité: "+m);
    }

    @GetMapping("/{typeActiviteId}/sites")
    public ResponseEntity<?> getSitesByTypeActivite(@PathVariable Long typeActiviteId) {
        try {
            Set<siteDTO> sites = ser.getSitesByTypeActivite(typeActiviteId);

            if (sites.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun site n'a ce type d'activité.");
            } else {
                return ResponseEntity.ok().body(sites);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le type d'activité spécifié n'existe pas.");
        }
    }
    @PutMapping("/{siteId}/type-activites")
    public ResponseEntity<String> updateSiteTypeActivite(@PathVariable Long siteId, @RequestBody List<Long> typeActiviteIds) {
        try {
            ser.updateSiteTypeActivite(siteId, typeActiviteIds);
            return ResponseEntity.ok("Le site a été modifié avec succès.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le site n'existe pas.");
        }
    }

    @GetMapping ("/sitesMu")
    public ResponseEntity<?> getSitesByMultipleTypeActivites(@RequestParam List<Long> typeActiviteIds) {
        Set<siteDTO> sites = ser.getSitesByMultipleTypeActivites(typeActiviteIds);

        if (sites.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun site n'a ces types d'activité.");
        } else {
            return ResponseEntity.ok().body(sites);
        }
    }

    @DeleteMapping("/{siteId}/type-activites")
    public ResponseEntity<String> clearTypeActiviteForSite(@PathVariable Long siteId) {
        boolean success = ser.clearTypeActiviteForSite(siteId);
        if (success) {
            return ResponseEntity.ok("Les types d'activités pour le site ont été effacés avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le site avec l'ID spécifié n'a pas été trouvé.");
        }
    }

    @DeleteMapping("/{typeActiviteId}/sites")
    public ResponseEntity<String> clearSitesForTypeActivite(@PathVariable Long typeActiviteId) {
        boolean success = ser.clearSitesForTypeActivite(typeActiviteId);
        if (success) {
            return ResponseEntity.ok("Les sites pour le type d'activité ont été effacés avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le type d'activité avec l'ID spécifié n'a pas été trouvé.");
        }
    }

    @PostMapping("/upload-customers-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file){
        this.ser.saveCustomersToDatabase(file);
        return ResponseEntity
                .ok(Map.of("Message" , " Customers data uploaded and saved to database successfully"));
    }
    @GetMapping("/activitites/export/excel")
    public void exportToExcel(HttpServletResponse servletResponse) throws IOException {
        List<typeActiviteDTO> activiteDTOS = ser.allActivities();
        ActivitieExcelExporter exporter = new ActivitieExcelExporter();
        exporter.export(activiteDTOS, servletResponse);
    }

    @GetMapping("/activitites/export/pdf")
    public void exportToPDF(HttpServletResponse servletResponse) throws IOException {
        List<typeActiviteDTO> activiteDTOS = ser.allActivities();
        ActivitiePDFExporter exporter = new ActivitiePDFExporter();
        exporter.export(activiteDTOS, servletResponse);
    }

    @GetMapping("/activitites/export/CSV")
    public void exportToCSV(HttpServletResponse servletResponse) throws IOException {
        List<typeActiviteDTO> activiteDTOS = ser.allActivities();
        ActivitieCSVExporter exporter = new ActivitieCSVExporter();
        exporter.export(activiteDTOS, servletResponse);
    }

    @PostMapping("/import-sites/{id}")
    public ResponseEntity<?> importSitesParTypeActivite(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Veuillez sélectionner un fichier.", HttpStatus.BAD_REQUEST);
        }

        try {
            importerSite.importSitesParTypeActivite(file.getInputStream(), id);
            return new ResponseEntity<>("Les sites ont été importés avec succès.", HttpStatus.OK);
        } catch (ActiviteNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Une erreur s'est produite lors de l'importation des sites.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException ex) {
            String errorMessage = "Un site avec le même nom existe déjà dans la base de données.";
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }
    }


}