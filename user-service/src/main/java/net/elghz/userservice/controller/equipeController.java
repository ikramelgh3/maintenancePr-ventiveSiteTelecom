package net.elghz.userservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.userservice.dtos.EquipeTechnicienDTO;
import net.elghz.userservice.service.equipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
public class equipeController {

     private equipeService service;


    @PostMapping("/add/equipe")
    public ResponseEntity<?>addEquipe(@RequestBody EquipeTechnicienDTO equipeDTO) {
        return service.addEquipe(equipeDTO);
    }


    @PostMapping("/equipe/add")
        public ResponseEntity<?> addEquipeWithTechniciensController(@RequestBody EquipeTechnicienDTO dto, @RequestParam List<Long> techniciensID) {
        ResponseEntity<?> response = service.addEquipeWithTechniciens(dto, techniciensID);
        return response;
    }

    @GetMapping("/equipe/{id}")
    public EquipeTechnicienDTO  findEquipe(@PathVariable Long id) {
        return service.findEquipe(id);
    }

    @DeleteMapping("/delete/equipe/{id}")
    public ResponseEntity<?> deleteEquipe(@PathVariable Long id) {
        return service.deleteEquippe(id);
    }

    @GetMapping("/equipe/all")
    public ResponseEntity<?> getAllEquipes() {
        return service.getAll();
    }



    @GetMapping("/{equipeId}/techniciens")
    public ResponseEntity<?> getTechniciensDeLEquipe(@PathVariable Long equipeId) {
        return service.getTechnicienEquipe(equipeId);
    }

    @DeleteMapping("/dissocier/{equipeId}/techniciens/{technicienId}")
    public ResponseEntity<?> dissocierTechnicienFromEquipe(@PathVariable Long equipeId, @PathVariable Long technicienId) {
        return service.dissocierTechnicienFromEquipe(equipeId, technicienId);
    }

    @GetMapping("/equipe/{equipeId}/competences")
    public ResponseEntity<?> getCompetenceOfEquipe(@PathVariable Long equipeId) {
        return service.getCompetenceOfEquipe(equipeId);
    }
}
