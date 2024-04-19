package net.elghz.planningmaintenance.service;

import lombok.AllArgsConstructor;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.mapper.mapper;
import net.elghz.planningmaintenance.repository.planningRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class planningService {

    private planningRepo repo;
    private mapper mp;

    public ResponseEntity<?> addPlanning(PlanningMaintenanceDTO pl){
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){
            repo.save(pln);
            return new ResponseEntity<>("Le planning est bien ajouté." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja un planning avec ce nom." , HttpStatus.OK);
    }


    public ResponseEntity<?> findPlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        PlanningMaintenanceDTO  dto = mp.from(planningMaintenance.get());

        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }

    public ResponseEntity<?> deletePlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        repo.delete(planningMaintenance.get());

        return  new ResponseEntity<>("Le planning est bien supprimé" , HttpStatus.OK);
    }



    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(repo.findAll().stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }


    public ResponseEntity<?> findByStatus(PlanningStatus status){
         return  new ResponseEntity<>(repo.findByStatus(status).stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?>  getStatusOfPlanning( Long id){
        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(planningMaintenance.get().getStatus(), HttpStatus.OK);
    }

    public ResponseEntity<?>  updateStatusOfPlanning( Long id , PlanningStatus st){
        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        planningMaintenance.get().setStatus(st);
        repo.save(planningMaintenance.get());

        return  new ResponseEntity<>("Le status est bien modifie", HttpStatus.OK);
    }


}
