package net.elghz.checklistservice.controller;

import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.mapper.mapper;
import net.elghz.checklistservice.repository.checklistRepo;
import net.elghz.checklistservice.services.checklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pointMesure")
public class checklistController {

    @Autowired
    checklistRepo repo;

    @Autowired
    mapper mp;
    @PostMapping("/add/checklist")
    public  ChecklistDTO addCHecklist(@RequestBody ChecklistDTO dt){
      Checklist c = mp.from(dt);
         repo.save(c);
         return  dt;
    }
//
//    @Autowired
//    checklistService ser;
//    @GetMapping("/checklist/all")
//
//    public ResponseEntity<?> findAll(){
//         return ser.allChecklist();
//    }
//    //à utiliser associe checklist au responsable
//    @PostMapping ("/checklist/PointMesure/add")
//    public ResponseEntity<?> addChecklistComplet(@RequestBody ChecklistDTO ch , @RequestBody Long idRes ,@RequestBody Long idE){
//        return  ser.ajouterChecklistComplet(ch, idRes , idE);
//    }
//    @PostMapping ("/checklist/add")
//    public ResponseEntity<?> addChecklist(@RequestBody ChecklistDTO ch){
//        return  ser.addChecklist(ch);
//    }
//
//    @PostMapping ("/checklist/add/{id}")
//    public ResponseEntity<?> addChecklistRspo(@RequestBody ChecklistDTO ch, @PathVariable Long id ){
//        return  ser.ajouterChecklist(ch, id);
//    }
//    @GetMapping("/checklist/id/{id}")
//    public ChecklistDTO findById(@PathVariable Long id){
//        return  ser.findById(id);
//    }
//    @DeleteMapping("/checklist/delete/{id}")
//    public ResponseEntity<?> deleteCk(@PathVariable Long id){
//         return  ser.deleteChecklist(id);
//    }
//    @PutMapping ("/checklist/update/{id}")
//    public ResponseEntity<?> updateCheckList(@PathVariable Long id){
//        return ser.updateCHeck(id);
//    }
//
//    @PutMapping("/add/PointMesure/checklist/{id}")
//    public ResponseEntity<?> addPointToChecklist(@PathVariable Long id , @RequestBody List<PointMesureDTO> pt){
//        return  ser.ajouterPointMesuresToChecklist(id,pt);
//    }
//
//    @PutMapping("/ajouter/PointMesure/checklist/{id}")
//    public ResponseEntity<?> ajouterPointToChecklist(@PathVariable Long id , @RequestBody List<Long> pt){
//        return  ser.addPointMesuresToChecklist(id,pt);
//    }
//    @PutMapping("/delete/PointMesure/Checklist/{id}")
//    public ResponseEntity<?> deletePointToChecklist(@PathVariable Long id , @RequestBody PointMesureDTO pt){
//        return  ser.supprimerPointMesureToChecklist(id,pt);
//    }
//
//    @GetMapping("/checklists/respo/{idR}")
//    List<ChecklistDTO> getChecklistsByResp( @PathVariable Long idR){
//        return ser.getChecklistByRespo(idR);
//    }
//
//    @GetMapping("/checklists/equip")
//    List<ChecklistDTO> getChecklistsByEqui( @RequestParam Long idEqui){
//        return ser.getChecklistByEquip(idEqui);
//    }
//
//

}
