package net.elghz.interventionservice.controler;

import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.enumeration.PrioriteEnum;
import net.elghz.interventionservice.enumeration.TypeIntervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.mapper.mapper;
import net.elghz.interventionservice.model.TechnicienDTO;
import net.elghz.interventionservice.model.checklist;
import net.elghz.interventionservice.repository.interventionRepo;
import net.elghz.interventionservice.service.interventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interventions")

@AllArgsConstructor
public class interventionController {


    interventionService service;
    @Autowired
    interventionRepo repo;


    @GetMapping("/techniciens/by/city/equipement/{id}")
    public List<TechnicienDTO> getTechnicenExistantVilleEqui(@PathVariable LocalDate id){
         return  service.getAvailableTechniciansForDay(id);
    }
    @PostMapping ("/ajouterIntervention/{idTech}/{idPl}/{idEq}/{idRespo}")
    public InterventionDTO ajouterIntr(@RequestBody InterventionDTO dt, @PathVariable String idTech , @PathVariable Long idPl , @PathVariable Long idEq,@PathVariable String idRespo){
        return service.addInterventionComplet(dt, idTech , idPl ,idEq, idRespo);
    }


    @GetMapping("/interventionsT/{type}")
    public List<InterventionDTO> getInterventionByType(@PathVariable TypeIntervention type){
         return  service.getInterventionByType(type);
    }

    @GetMapping("/interventionsS/{statut}")
    public List<InterventionDTO> getInterventionByStatus( @PathVariable statusIntervention statut){
        return  service.getInterventionByStatus(statut);
    }


    @GetMapping("/interventionsP/{priorite}")
    public List<InterventionDTO> getInterventionByPriorite(@PathVariable PrioriteEnum priorite){
        return  service.getInterventionByPriorite(priorite);
    }
@Autowired
mapper mp;
    @GetMapping("/interventionsK/{keyword}")
    public List<InterventionDTO> getInterventionByNom(@PathVariable String keyword){
        return  repo.findAllPln(keyword).stream().map(mp::from).collect(Collectors.toList());
    }




    @GetMapping("/intervention/technicien/{id}")
    public List<InterventionDTO> getInterventionByTEchn(@PathVariable  String id){
        return  service.getInterventionsOfTehnicien(id);
    }

    @GetMapping("/checklist/point/{idTy}")
    public checklist  checklistByIdTY(@PathVariable Long idTy){
         return service.poinMesureByTypeEqui(idTy);
    }
    @PostMapping ("/ajouterInterventionCorrective/{idTech}/{idEq}/{idRespo}")
    public InterventionDTO ajouterIntrCorrec(@RequestBody InterventionDTO dt, @PathVariable String idTech , @PathVariable Long idEq,@PathVariable String idRespo){
        return service.addInterventionCompletCorrective(dt, idTech  ,idEq, idRespo);
    }
    @GetMapping ("/send/email/technic/{idTec}/{idIn}/{idRespo}")

    public  void sendEmail(@PathVariable String idTec , @PathVariable Long idIn , @PathVariable String idRespo){
         service.sendEmailToTechnicien(idTec , idIn, idRespo);
    }
    @GetMapping("/check-existenceIn/{name}")
    public Boolean checkexosy(@PathVariable String name){
        return service.checkPlanningExists(name);
    }

    @PostMapping("/add/Inter")
    public ResponseEntity<?> addInter(@RequestBody InterventionDTO dt){
        return  service.addIntervention(dt);
    }

//commun with planning
    @PostMapping("/add/Intervention/{idPlanning}")
    public Boolean addInterToPlanning(@PathVariable Long idPlanning,@RequestBody InterventionDTO dt){
          return service.ajouterInterventionToPlanning(idPlanning,dt);}

    @GetMapping("/get/all/Inter")
    public List<InterventionDTO>getAllInter(){
        return service.getAll();
    }

    @GetMapping("/find/Inter/{id}")
    public ResponseEntity<?> findInterById(@PathVariable Long id){
        return  service.findInterv(id);
    }

    @GetMapping("/find/Intervention/{id}")
    public InterventionDTO findInterventionById(@PathVariable Long id){
        return  service.findIntervention(id);
    }


    @GetMapping("/nbreTot")
    public int nobreTot(){
        return service.getAll().size();
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
    public ResponseEntity<?> attribuerEquipeIntervention(@PathVariable Long idI, @PathVariable String idE){
        return service.attribuerEquipeToIntervention(idI, idE);
    }

    @DeleteMapping("/dissocier/intervention/{idI}/equipe/{idE}")
    public ResponseEntity<?> dissocierEquipeIntervention(@PathVariable Long idI, @PathVariable Long idE){
        return service.dissosierEquipeToIntervention(idI, idE);
    }

    @GetMapping("/get/interventions/ofEqui/{id}")
    public   List<InterventionDTO> getInterventionsOfEquipement(@PathVariable Long id){
         return service.getInterventionOfEqui(id);
    }

}
