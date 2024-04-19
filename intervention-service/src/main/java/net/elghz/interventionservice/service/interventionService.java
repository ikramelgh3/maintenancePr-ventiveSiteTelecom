package net.elghz.interventionservice.service;

import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.mapper.mapper;
import net.elghz.interventionservice.repository.interventionRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class interventionService {

    private interventionRepo repo;
    private mapper mp;

    public ResponseEntity<?> addIntervention(InterventionDTO pl){
        Intervention pln = mp.from(pl);
        Optional<Intervention> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){
            repo.save(pln);
            return new ResponseEntity<>("L'intervention est bien planifiée." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja une intervention avec ce nom." , HttpStatus.OK);
    }


    public ResponseEntity<?> findInterv( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());

        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }

    public ResponseEntity<?> deleteInte( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        repo.delete(planningMaintenance.get());

        return  new ResponseEntity<>("L'intervention est bien supprimé" , HttpStatus.OK);
    }



    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(repo.findAll().stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }


    public ResponseEntity<?> findByStatus(statusIntervention status){
        return  new ResponseEntity<>(repo.findByStatus(status).stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?>  getStatusOfInter( Long id){
        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(planningMaintenance.get().getStatus(), HttpStatus.OK);
    }

    public ResponseEntity<?>  updateStatusOfInter( Long id , statusIntervention st){
        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        planningMaintenance.get().setStatus(st);
        repo.save(planningMaintenance.get());

        return  new ResponseEntity<>("Le status est bien modifie", HttpStatus.OK);
    }




}
