package net.elghz.interventionservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.feign.PlannigRestClient;
import net.elghz.interventionservice.feign.TechnicienRestClient;
import net.elghz.interventionservice.feign.equipementRestClient;
import net.elghz.interventionservice.mapper.mapper;
import net.elghz.interventionservice.model.*;
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
    private TechnicienRestClient erepo;
    private equipementRestClient eqRepo;

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
            pln.setPlanning(prepo.findPlanningById(IdPlanning));
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
        Long idEqui = dto.getId_Techn();
        TechnicienDTO e = erepo.findTechnicienById(idEqui);
        dto.setTechnicien(e);
        Equipement equipement = eqRepo.getEquiById(dto.getId_Equipement());
        dto.setEquipement(equipement);
        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }


    public InterventionDTO findIntervention( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return null;
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());
        Long idEqui = dto.getId_Techn();

        if(idEqui==null){
            return  dto;
        }
        TechnicienDTO e = erepo.findTechnicienById(idEqui);
        if(e==null){
            return  dto;
        }
        Equipement eq = eqRepo.getEquiById(dto.getId_Equipement());
        if(e==null){
            return  dto;
        }
        dto.setEquipement(eq);
        dto.setTechnicien(e);
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
            Long idEqui = intervention.getId_Techn();
            if (idEqui == null) {
                intervention.setTechnicien(null);
                continue;
            }
            TechnicienDTO equipe = erepo.findTechnicienById(idEqui);
            if (equipe == null) {
                intervention.setTechnicien(null);
            } else {
                intervention.setTechnicien(equipe);
            }

            Equipement equipement = eqRepo.getEquiById(intervention.getId_Equipement());
            if (equipement == null) {
                intervention.setEquipement(null);
            } else {
                intervention.setEquipement(equipement);
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

        TechnicienDTO e = erepo.findTechnicienById(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Techn(idEqui);
        intervention.get().setTechnicien(e);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien attribue à l'interevntion: "+idInte , HttpStatus.OK);


    }

    public ResponseEntity<?> dissosierEquipeToIntervention(Long idInte , Long idEqui ){

        Optional<Intervention> intervention = repo.findById(idInte);
        if (!intervention.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+idInte , HttpStatus.NOT_FOUND);
        }

        TechnicienDTO e = erepo.findTechnicienById(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Techn(null);
        intervention.get().setTechnicien(null);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien dessocier  de l'interevntion: "+idInte , HttpStatus.OK);


    }

    public List<InterventionDTO> getInterventionOfEqui(Long idEqui){
         List<Intervention> interventions = repo.findById_Equipement(idEqui);
         for(Intervention i :interventions){
              Equipement eq = eqRepo.getEquiById(i.getId_Equipement());
              i.setEquipement(eq);
              TechnicienDTO tech = erepo.findTechnicienById(i.getId_Techn());
              i.setTechnicien(tech);
         }
         return  interventions.stream().map(mp::from).collect(Collectors.toList());
    }
}
