package net.elghz.planningmaintenance.controler;

import jakarta.ws.rs.NotFoundException;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.exception.PlanningNameExistsException;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.service.planningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class planningControler {

     @Autowired private planningService service;


     @PostMapping("/add/planning")
    public PlanningMaintenanceDTO addPlanning(@RequestBody PlanningMaintenanceDTO dt){
          return  service.addPlanning(dt);
     }

     @GetMapping("size/plannings")
     public int size(){
          return service.getSize();
     }
    @PostMapping("/add/planningComplet/{idRes}/{idSite}")
    public  PlanningMaintenanceDTO addPlanningComplet(@RequestBody PlanningMaintenanceDTO dt , @PathVariable Long idRes, @PathVariable Long idSite){
        return  service.addPlanningComplet(dt , idRes , idSite);
    }

    @PostMapping("/add/planningComp/{idRes}")
    public PlanningMaintenanceDTO addPlanningCom(@RequestBody PlanningMaintenanceDTO dt , @PathVariable Long idRes){
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

    @PatchMapping("/updatePlanning/{id}")
    public ResponseEntity<?> updatePlanning(@PathVariable Long id, @RequestBody PlanningMaintenanceDTO planningDTO) {
        try {
            PlanningMaintenance updatedPlanning = service.updatePlanning(id, planningDTO);
            return new ResponseEntity<>(updatedPlanning, HttpStatus.OK);
        } catch (PlanningNameExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur s'est produite lors de la mise à jour du planning", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/check-existence/{name}")
    public boolean checkPlanningExistence(@PathVariable String name) {
        boolean exists = service.checkPlanningExists(name);
        return exists;
    }

    //get plannings of respo
    @GetMapping("/get/planningOfRespo/{idResp}")
    public List<PlanningMaintenanceDTO> planningOfRespo(@PathVariable Long idResp){
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



}
