package net.elghz.planningmaintenance.controler;

import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.service.planningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class planningControler {

     @Autowired private planningService service;

     @PostMapping("/add/planning")
    public ResponseEntity<?> addPlanning(@RequestBody PlanningMaintenanceDTO dt){
          return  service.addPlanning(dt);
     }

     @GetMapping("/get/all/plannings")
     public ResponseEntity<?> getAllPlanning(){
         return service.getAll();
     }

     @GetMapping("/find/planning/{id}")
    public ResponseEntity<?> findPlanningById(@PathVariable Long id){
          return  service.findPlanning(id);
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


}
