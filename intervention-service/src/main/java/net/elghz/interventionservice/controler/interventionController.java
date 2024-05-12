package net.elghz.interventionservice.controler;

import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.model.technicien;
import net.elghz.interventionservice.service.interventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@AllArgsConstructor
public class interventionController {


    interventionService service;


    @PostMapping("/add/Inter")
    public ResponseEntity<?> addInter(@RequestBody InterventionDTO dt){
        return  service.addIntervention(dt);
    }

//commun with planning
    @PostMapping("/add/Intervention/{idPlanning}")
    public Boolean addInterToPlanning(@PathVariable Long idPlanning,@RequestBody InterventionDTO dt){
          return service.ajouterInterventionToPlanning(idPlanning,dt);}

    @GetMapping("/get/all/Inter")
    public ResponseEntity<?> getAllInter(){
        return service.getAll();
    }

    @GetMapping("/find/Inter/{id}")
    public ResponseEntity<?> findInterById(@PathVariable Long id){
        return  service.findInterv(id);
    }

    @GetMapping("/find/Intervention")
    public InterventionDTO findInterventionById(@RequestParam Long id){
        return  service.findIntervention(id);
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

    @GetMapping("/interventions/planning/{idPlanning}")
    List<InterventionDTO> getInterventionsOfPlanning(@PathVariable Long idPlanning){

        return service.getInterventionOfPlanning(idPlanning);
    }

    @GetMapping("/all/Interventions/Equipe/{id}")
    public ResponseEntity<?> allInterventionsOfEquipe(@PathVariable Long id){

        List<InterventionDTO> interventionDTOS = service.getInterventionOfEquipe(id);
        if (interventionDTOS.size()==0){
            return  new ResponseEntity<>("Aucune intervention n'est trouvé pour cet equipe." , HttpStatus.OK);

        }
        return  new ResponseEntity<>(interventionDTOS , HttpStatus.OK);
    }


    @PostMapping("/attribuer/intervention/{idI}/equipe/{idE}")
    public ResponseEntity<?> attribuerEquipeIntervention(@PathVariable Long idI, @PathVariable Long idE){
        return service.attribuerEquipeToIntervention(idI, idE);
    }

    @DeleteMapping("/dissocier/intervention/{idI}/equipe/{idE}")
    public ResponseEntity<?> dissocierEquipeIntervention(@PathVariable Long idI, @PathVariable Long idE){
        return service.dissosierEquipeToIntervention(idI, idE);
    }


    @GetMapping("get/interventions/ofEqui/{id}")
    public  List<InterventionDTO> getInterventionsOfEquipement(@PathVariable Long id){
         return service.getInterventionOfEqui(id);
    }

}
