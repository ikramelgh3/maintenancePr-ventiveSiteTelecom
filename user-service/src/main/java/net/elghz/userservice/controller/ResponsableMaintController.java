package net.elghz.userservice.controller;

import net.elghz.userservice.dtos.responsableDTO;
import net.elghz.userservice.entities.ResponsableMaintenance;
import net.elghz.userservice.service.responsableMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResponsableMaintController {
  @Autowired
  private responsableMaintenanceService ser;

  @GetMapping("/all/Respo")
  public List<responsableDTO> getAll(){
    return ser.responsables();
  }
    @PostMapping("/add/respo")
    public ResponseEntity<?> addResp(@RequestBody responsableDTO resp){
         return  ser.ajouterRespo(resp);
    }

  @GetMapping("/respo/id/{id}")
  public responsableDTO findById(@PathVariable Long id){
    return  ser.findById(id);
  }

  @GetMapping("/checklist/respo/{username}")
  public ResponseEntity<?> checklistsByRespo(@PathVariable String username){
    return  ser.getChecklistOfResponMaint(username);
  }


}
