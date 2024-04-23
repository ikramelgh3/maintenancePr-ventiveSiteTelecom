package net.elghz.interventionservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.feign.EquipeRestClient;
import net.elghz.interventionservice.feign.PlannigRestClient;
import net.elghz.interventionservice.mapper.mapper;
import net.elghz.interventionservice.model.Equipe;
import net.elghz.interventionservice.model.Planning;
import net.elghz.interventionservice.model.technicien;
import net.elghz.interventionservice.repository.interventionRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class interventionService {

    private interventionRepo repo;
    private mapper mp;
    private PlannigRestClient prepo;
    private EquipeRestClient erepo;

    public ResponseEntity<?> addIntervention(InterventionDTO pl){
        Intervention pln = mp.from(pl);
        Optional<Intervention> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){
            repo.save(pln);
            return new ResponseEntity<>("L'intervention est bien planifiée." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja une intervention avec ce nom." , HttpStatus.OK);
    }

    public Boolean ajouterInterventionToPlanning(Long IdPlanning, InterventionDTO pl){
        Intervention pln = mp.from(pl);
        Optional<Intervention> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){

            pln.setId_Planning(IdPlanning);
            pln.setPlanning(prepo.findPlanningId(IdPlanning));
            repo.save(pln);
            return true;

        }
        else {
           return false;
        }



    }

    public ResponseEntity<?> findInterv( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());
        Long idEqui = dto.getId_Equipe();
        Equipe e = erepo.findEquipe(idEqui);
        dto.setEquipe(e);

        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }


    public InterventionDTO findIntervention( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return null;
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());
        Long idEqui = dto.getId_Equipe();

        if(idEqui==null){
            return  dto;
        }
        Equipe e = erepo.findEquipe(idEqui);
        if(e==null){
            return  dto;
        }
        dto.setEquipe(e);
        return  dto;
    }

    public ResponseEntity<?> deleteInte( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        repo.delete(planningMaintenance.get());

        return  new ResponseEntity<>("L'intervention est bien supprimé" , HttpStatus.OK);
    }



    public ResponseEntity<?> getAll() {
        List<Intervention> interventions = repo.findAll();
        for (Intervention intervention : interventions) {
            Long idEqui = intervention.getId_Equipe();
            if (idEqui == null) {
                intervention.setEquipe(null);
                continue;
            }
            Equipe equipe = erepo.findEquipe(idEqui);
            if (equipe == null) {
                intervention.setEquipe(null);
            } else {
                intervention.setEquipe(equipe);
            }
            repo.save(intervention);
        }
        List<InterventionDTO> interventionDTOs = interventions.stream()
                .map(mp::from)
                .collect(Collectors.toList());
        return new ResponseEntity<>(interventionDTOs, HttpStatus.OK);
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


    public List<InterventionDTO> getInterventionOfPlanning( Long idPl){
        return repo.findById_Planning(idPl).stream().map(mp::from).collect(Collectors.toList());
    }


    public List<InterventionDTO> getInterventionOfEquipe( Long idEquipe){
        return repo.findById_Equipe(idEquipe).stream().map(mp::from).collect(Collectors.toList());
    }

    public ResponseEntity<?> attribuerEquipeToIntervention(Long idInte , Long idEqui ){

        Optional<Intervention> intervention = repo.findById(idInte);
        if (!intervention.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+idInte , HttpStatus.NOT_FOUND);
        }

        Equipe e = erepo.findEquipe(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Equipe(idEqui);
        intervention.get().setEquipe(e);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien attribue à l'interevntion: "+idInte , HttpStatus.OK);


    }

    public ResponseEntity<?> dissosierEquipeToIntervention(Long idInte , Long idEqui ){

        Optional<Intervention> intervention = repo.findById(idInte);
        if (!intervention.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+idInte , HttpStatus.NOT_FOUND);
        }

        Equipe e = erepo.findEquipe(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Equipe(null);
        intervention.get().setEquipe(null);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien dessocier  de l'interevntion: "+idInte , HttpStatus.OK);


    }
}
