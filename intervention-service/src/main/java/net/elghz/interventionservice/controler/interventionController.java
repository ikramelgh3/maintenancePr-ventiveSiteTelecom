package net.elghz.interventionservice.controler;

import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.service.interventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class interventionController {

    @Autowired
    interventionService service;


    @PostMapping("/add/Inter")
    public ResponseEntity<?> addInter(@RequestBody InterventionDTO dt){
        return  service.addIntervention(dt);
    }

    @GetMapping("/get/all/Inter")
    public ResponseEntity<?> getAllInter(){
        return service.getAll();
    }

    @GetMapping("/find/Inter/{id}")
    public ResponseEntity<?> findInterById(@PathVariable Long id){
        return  service.findInterv(id);
    }

    @GetMapping("/get/Inter/{status}")
    public ResponseEntity<?> getInterWithStatus(@PathVariable statusIntervention status){
        return  service.findByStatus(status);
    }

    @DeleteMapping("/delete/Inter/{id}")
    public ResponseEntity<?> deleteInter(@PathVariable Long id){
        return  service.deleteInte(id);
    }

    @GetMapping("/get/status/Inter/{id}")
    public ResponseEntity<?> getStatusInter(@PathVariable Long id){
        return  service.getStatusOfInter(id);
    }

    @PutMapping("/Inter/{id}/status/{status}")
    public ResponseEntity<?> updateStatusOfInter(@PathVariable Long id, @PathVariable statusIntervention status) {
        return service.updateStatusOfInter(id, status);

    }




}
