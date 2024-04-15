package net.elghz.checklistservice.controller;

import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.services.checklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class checklistController {

    @Autowired
    checklistService ser;
    @GetMapping("/checklist/all")
    public ResponseEntity<?> findAll(){
         return ser.allChecklist();
    }
    @PostMapping ("/checklist/PointMesure/add")
    public ResponseEntity<?> addChecklistComplet(@RequestBody ChecklistDTO ch ){
        return  ser.ajouterChecklistComplet(ch);
    }
    @PostMapping ("/checklist/add")
    public ResponseEntity<?> addChecklist(@RequestBody ChecklistDTO ch){
        return  ser.addChecklist(ch);
    }
    @GetMapping("/checklist/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
         return  ser.findById(id);
    }
    @DeleteMapping("/checklist/delete/{id}")
    public ResponseEntity<?> deleteCk(@PathVariable Long id){
         return  ser.deleteChecklist(id);
    }
    @PutMapping ("/checklist/update/{id}")
    public ResponseEntity<?> updateCheckList(@PathVariable Long id){
        return ser.updateCHeck(id);
    }

    @PutMapping("/add/PointMesure/checklist/{id}")
    public ResponseEntity<?> addPointToChecklist(@PathVariable Long id , @RequestBody List<PointMesureDTO> pt){
        return  ser.ajouterPointMesuresToChecklist(id,pt);
    }

    @PutMapping("/ajouter/PointMesure/checklist/{id}")
    public ResponseEntity<?> ajouterPointToChecklist(@PathVariable Long id , @RequestBody List<Long> pt){
        return  ser.addPointMesuresToChecklist(id,pt);
    }
    @PutMapping("/delete/PointMesure/Checklist/{id}")
    public ResponseEntity<?> deletePointToChecklist(@PathVariable Long id , @RequestBody PointMesureDTO pt){
        return  ser.supprimerPointMesureToChecklist(id,pt);
    }

}
