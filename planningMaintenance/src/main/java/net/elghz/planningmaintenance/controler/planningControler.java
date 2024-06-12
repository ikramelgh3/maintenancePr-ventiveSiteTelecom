package net.elghz.planningmaintenance.controler;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.exception.PlanningNameExistsException;
import net.elghz.planningmaintenance.export.PlanningsExcelExporter;
import net.elghz.planningmaintenance.importFile.ImporterPlanning;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.service.planningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/planningsMaintenances")

public class planningControler {

     @Autowired private planningService service;
     @Autowired private ImporterPlanning importerPlanning;
@GetMapping("/auth")
    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    @PostMapping("/add/planning")
    public PlanningMaintenanceDTO addPlanning(@RequestBody PlanningMaintenanceDTO dt){
          return  service.addPlanning(dt);
     }


    @PostMapping("/add/planningComplet/{idRes}/{idSite}")
    public  PlanningMaintenanceDTO addPlanningComplet(@RequestBody PlanningMaintenanceDTO dt , @PathVariable String idRes, @PathVariable Long idSite){
        return  service.addPlanningComplet(dt , idRes , idSite);
    }

    @PostMapping("/add/planningComp/{idRes}")
    public PlanningMaintenanceDTO addPlanningCom(@RequestBody PlanningMaintenanceDTO dt , @PathVariable String idRes){
        return  service.addPlanningCom(dt , idRes );
    }


    @GetMapping("/get/all/plannings")
     public List<PlanningMaintenanceDTO> getAllPlanning(){
         return service.getAll();
     }


     @GetMapping("/find/planning/{id}")
    public PlanningMaintenanceDTO findPlanningById(@PathVariable Long id){
          return  service.findPlanningByIdWithDetails(id);
     }

    @GetMapping("/find/planningBy/{id}")
    public PlanningMaintenanceDTO findPlanningId(@PathVariable Long id){
        return  service.findPlanningById(id);
    }

     @GetMapping("/get/plannings/{status}")
    public List<PlanningMaintenanceDTO>getPlanningWithStatus(@PathVariable PlanningStatus status){
          return  service.findByStatus(status);
     }

     @GetMapping("planning/type/{type}")
     public List<PlanningMaintenanceDTO> getPlanningByTypeSite(@PathVariable String type){
         return service.planningByTypeSite(type);
     }
     @DeleteMapping("/delete/planning/{id}")
    public void deletePlanning(@PathVariable Long id){
           service.deletePlanning(id);
     }

    @GetMapping("/get/status/{id}")
    public ResponseEntity<?> getStatusPlanning(@PathVariable Long id){
        return  service.getStatusOfPlanning(id);
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatusOfPlanning(@PathVariable Long id, @PathVariable PlanningStatus status) {
         return service.updateStatusOfPlanning(id, status);

    }
    //get les planning of site
    @GetMapping("/get/planningOfSite/{idSite}")
    public List<PlanningMaintenanceDTO> planningOfSite(@PathVariable Long idSite){
         return service.getPlanningsOfSite(idSite);
    }

    @PatchMapping("/updatePlanning/{id}/{idSite}/{idResp}")
    public ResponseEntity<?> updatePlanning(@PathVariable Long id, @RequestBody PlanningMaintenanceDTO planningDTO , @PathVariable Long idSite , @PathVariable String idResp) {
        try {
            PlanningMaintenance updatedPlanning = service.updatePlanning(id, planningDTO , idSite , idResp);
            return new ResponseEntity<>(updatedPlanning, HttpStatus.OK);
        } catch (PlanningNameExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur s'est produite lors de la mise à jour du planning", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find/by/semsetre/{semestre}")
    public List<PlanningMaintenanceDTO> findBySemestre(@PathVariable String  semestre){
          return service.findBySemestre(semestre);
    }
    @GetMapping("/check-existence/{name}")
    public boolean checkPlanningExistence(@PathVariable String name) {
        boolean exists = service.checkPlanningExists(name);
        return exists;
    }

    //get plannings of respo
    @GetMapping("/get/planningOfRespo/{idResp}")
    public List<PlanningMaintenanceDTO> planningOfRespo(@PathVariable String idResp){
         return service.getPlanningsOfResp(idResp);
    }

    @PostMapping ("/add/intervention/Planning/{idPlanning}")
    public ResponseEntity<?> addInterventionToPlanning(@PathVariable Long idPlanning , @RequestBody Intervention intervention){
         return service.addInterventionToPlanning(idPlanning, intervention);
    }

    @GetMapping("/get/intervention/planning/{id}")
    public List<Intervention> getInterventionOfPlanning(@PathVariable Long id){
         return service.getInterventionOfPlanning(id);
    }

    @GetMapping("/find/Plannings/byKeyword/{keyword}")
    public List<PlanningMaintenanceDTO> getPlanningByKeyword(@PathVariable String keyword){
         return service.findByKeyword(keyword);
    }

    @GetMapping("/plannings/export/excel")
    public void exportToExcel( HttpServletResponse servletResponse) throws IOException {
        List<PlanningMaintenanceDTO> siteDTOS = service.getAll();
        PlanningsExcelExporter exporter = new PlanningsExcelExporter();
        exporter.export(siteDTOS, servletResponse);
    }

    @PostMapping("/import-planningd")
    public List<PlanningMaintenanceDTO> importSites(@RequestParam("file") MultipartFile file) {
        List<PlanningMaintenanceDTO> importedPlannings = new ArrayList<>();

        if (file.isEmpty()) {

            return null;
        }

        try {
            importedPlannings = importerPlanning.importPlanningd(file.getInputStream() );
            return importedPlannings;
        } catch (IOException e) {
            // Gérer l'erreur d'entrée/sortie
            e.printStackTrace();
            // Peut-être lever une exception ou retourner une liste vide selon votre logique métier
            return new ArrayList<>();
        } catch (DataIntegrityViolationException ex) {
            // Gérer l'erreur d'intégrité des données
            ex.printStackTrace();
            // Peut-être lever une exception ou retourner une liste vide selon votre logique métier
            return new ArrayList<>();
        }
    }

    @GetMapping("/intervention/site/{IdSite}")
    public List<Intervention> getnterventionsOfSite(@PathVariable Long IdSite){
         return service.interventionOfSite(IdSite);
    }

    @GetMapping("/size/plannings")
    public int getnbrePlannings(){
          return service.getNbrePlanning();
    }


}
