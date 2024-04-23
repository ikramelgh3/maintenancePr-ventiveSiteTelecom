package net.elghz.actioncorrectiveservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.actioncorrectiveservice.entities.ActionCorrective;
import net.elghz.actioncorrectiveservice.service.actionCorrectiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class actionCorrectiveController {
    private actionCorrectiveService service;

    @PostMapping("/add/actionCorrective")
    public ResponseEntity<?> addActionCorrective(@RequestBody ActionCorrective action , @RequestParam Long IdAn){
        return service.effectuerActionCorrective(action, IdAn);
    }

    @GetMapping("/get/actionCorrective/Technicien")
    public ResponseEntity<?> getTechnicienOfAction(@RequestParam Long idAC){
        return service.getTechnicienAction(idAC);
    }

    @GetMapping("/actionCorrective/byId")
    public ActionCorrective getActionById(@RequestParam Long id){
        return service.getActionById(id);
    }

    @GetMapping("/actionsCorrectives/Anomalie")
    public List<ActionCorrective> actionsCorrectivesOfAnomalie(@RequestParam Long id){
        return service.actionsCorrectivesAnomalies(id);
    }


    @DeleteMapping("/delete/ActionsCorrectives")
    public void deleteActionsCorrective(@RequestParam List<Long> idAcs ){
        service.deleteById(idAcs);
    }

}
