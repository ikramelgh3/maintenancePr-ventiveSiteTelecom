package net.elghz.planningmaintenance.controler;

import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.service.planningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class planningControler {

     @Autowired private planningService service;

     @PostMapping("/add/planning")
    public ResponseEntity<?> addPlanning(@RequestBody PlanningMaintenanceDTO dt){
          return  service.addPlanning(dt);
     }

    @PostMapping("/add/planningComplet/{idRes}/{idSite}")
    public ResponseEntity<?> addPlanningComplet(@RequestBody PlanningMaintenanceDTO dt , @PathVariable Long idRes, @PathVariable Long idSite){
        return  service.addPlanningComplet(dt , idRes , idSite);
    }

    @GetMapping("/get/all/plannings")
     public ResponseEntity<?> getAllPlanning(){
         return service.getAll();
     }

     @GetMapping("/find/planning/{id}")
    public ResponseEntity<?> findPlanningById(@PathVariable Long id){
          return  service.findPlanning(id);
     }

    @GetMapping("/find/planningBy/{id}")
    public PlanningMaintenanceDTO findPlanningId(@PathVariable Long id){
        return  service.findPlanningById(id);
    }

     @GetMapping("/get/plannings/{status}")
    public ResponseEntity<?> getPlanningWithStatus(@PathVariable PlanningStatus status){
          return  service.findByStatus(status);
     }

     @DeleteMapping("/delete/planning/{id}")
    public ResponseEntity<?> deletePlanning(@PathVariable Long id){
         return  service.deletePlanning(id);
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
