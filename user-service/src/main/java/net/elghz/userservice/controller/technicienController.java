package net.elghz.userservice.controller;

import net.elghz.userservice.dtos.CompetenceDTO;

import net.elghz.userservice.dtos.EquipeTechnicienDTO;
import net.elghz.userservice.dtos.TechnicienDTO;
import net.elghz.userservice.service.competenceService;

import net.elghz.userservice.service.technicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class technicienController {

    @Autowired
    technicienService ser;
    @GetMapping("/technicien/all")
    public ResponseEntity<?> findAll(){
         return  ser.getAllTechniciensWithCompetences();
    }
    @PostMapping ("/technicien/add")
    public ResponseEntity<?> addTechnicienAvecCompetence(@RequestBody TechnicienDTO ch){
        return  ser.ajouterTechnicienAvecCompetences(ch);
    }

    @PostMapping ("/techniciens/add")
    public ResponseEntity<?> addRoles(@RequestBody List<TechnicienDTO> ch){
        return  ser.addTechniciens(ch);
    }
    @GetMapping("/technicen/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
         return  ser.findById(id);
    }

    @GetMapping("/technicien/{id}")
    public TechnicienDTO findTechnicienById(@PathVariable Long id) {
        return ser.findTechnicienById(id);
    }
    @DeleteMapping("/technicien/delete/{id}")
    public ResponseEntity<?> deleteR(@PathVariable Long id){
         return  ser.deleteR(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TechnicienDTO dto){
        return ser.modifierTechnicien(id, dto);
    }

    @PostMapping("/{technicienId}/competences")
    public ResponseEntity<?> assignerCompetences(@PathVariable Long technicienId, @RequestBody List<Long> competenceIds) {
        return ser.assignerCompetences(technicienId, competenceIds);
    }

    @PutMapping("/{technicienId}/dissocier-competence/{competenceId}")
    public ResponseEntity<?> dissocierCompetence(@PathVariable Long technicienId, @PathVariable Long competenceId) {
        return ser.dissocierCompetence(technicienId, competenceId);
    }

    @GetMapping("/{technicienId}/competences")
    public ResponseEntity<?> getCompetencesByTechnicienId(@PathVariable Long technicienId) {
        return ser.getCompetencesByTechnicienId(technicienId);
    }

    @GetMapping("/competences/{competenceName}")
    public ResponseEntity<?> getTechniciensByCompetence(@PathVariable String competenceName) {
        return ser.getTechniciensByCompetence(competenceName);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<?> getTechniciensDisponibles() {
        return ser.getTechniciensDisponibles();
    }

    @PutMapping("/rendre/disponible/{id}")
    public ResponseEntity<?> rendreDispo(@PathVariable Long id){
        return ser.rendreTechnicienDisponible(id);
    }

    @GetMapping("/get/technicienDispo/WithCompetence")
    public List<TechnicienDTO> getTechnicienDispoWithCompetences(@RequestParam List<Long> idC){
        return ser.getTechnicienWithCompetencesAndDisponible(idC);
    }

    @GetMapping("/techniciens/internes")
    public List<TechnicienDTO> getTechniciensInternes(){

        return ser.getTechniciensInternes();
    }
    @GetMapping("/techniciens/externes")
    public List<TechnicienDTO> getTechniciensExterns(){

        return ser.getTechniciensExternes();
    }
}
