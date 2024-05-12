package net.elghz.userservice.service;

import lombok.AllArgsConstructor;
import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.dtos.EquipeTechnicienDTO;
import net.elghz.userservice.dtos.TechnicienDTO;
import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.EquipeIntervenant;
import net.elghz.userservice.entities.Technicien;
import net.elghz.userservice.mapper.mapper;
import net.elghz.userservice.repository.TechnicienRepository;
import net.elghz.userservice.repository.equipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class equipeService {
    /*
    private equipeRepository repo;
    private TechnicienRepository trepo;
    private mapper mp;

    public ResponseEntity<?> addEquipe(EquipeTechnicienDTO pl){
        EquipeIntervenant pln = mp.from(pl);
        Optional<EquipeIntervenant> equipe = repo.findById(pln.getId());
        if (!equipe.isPresent()){
            repo.save(pln);
            return new ResponseEntity<>("L'equipe est bien crée." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja une equipe avec ce id." , HttpStatus.OK);
    }



    public EquipeTechnicienDTO findEquipe( Long id){

        Optional<EquipeIntervenant> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

         return null ;
        }

        EquipeTechnicienDTO  dto = mp.from(planningMaintenance.get());

        return  dto;
    }



    public ResponseEntity<?> deleteEquippe(Long id) {
        Optional<EquipeIntervenant> planningMaintenanceOptional = repo.findById(id);
        if (planningMaintenanceOptional.isEmpty()) {
            return new ResponseEntity<>("Aucune equipe n'est trouvé avec ce id: " + id, HttpStatus.NOT_FOUND);
        }

        EquipeIntervenant planningMaintenance = planningMaintenanceOptional.get();
       // Set<Technicien> techniciens = planningMaintenance.getTechniciens();
        //Iterator<Technicien> iterator = techniciens.iterator();
//        while (iterator.hasNext()) {
//            Technicien technicien = iterator.next();
//            iterator.remove();
//           // technicien.getEquipes().remove(planningMaintenance);
//        }

        repo.delete(planningMaintenance);

        return new ResponseEntity<>("L'equipe est bien supprimé", HttpStatus.OK);
    }


    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(repo.findAll().stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }
    public ResponseEntity<?> addEquipeWithTechniciens(EquipeTechnicienDTO dto , List<Long> techniciensID){
        Optional<EquipeIntervenant> existingEquipe = repo.findById(dto.getId());
        if (existingEquipe.isPresent()){
            return new ResponseEntity<>("L'équipe existe déjà: " + dto.getId(), HttpStatus.BAD_REQUEST);
        }
        EquipeIntervenant equipeTechnicien = mp.from(dto);
        if( equipeTechnicien.getNbreTechnicien()!= techniciensID.size()){
            return new ResponseEntity<>("Le nombre de techniciens fournis ne correspond pas au nombre attendu pour cette équipe.", HttpStatus.BAD_REQUEST);
        }

        repo.save(equipeTechnicien);
        for (Long technicienId : techniciensID){
            Optional<Technicien> optionalTechnicien = trepo.findById(technicienId);
            if(optionalTechnicien.isEmpty()){
                return new ResponseEntity<>("Aucun technicien trouvé avec l'ID: " + technicienId, HttpStatus.NOT_FOUND);
            }
            Technicien technicien = optionalTechnicien.get();

                technicien.getEquipes().add(equipeTechnicien);
                equipeTechnicien.getTechniciens().add(technicien);
                trepo.save(technicien);
        }
        return new ResponseEntity<>("L'équipe a été ajoutée avec succès.", HttpStatus.OK);
    }


    public  ResponseEntity<?> getTechnicienEquipe(Long idEqui){
        Optional<EquipeIntervenant> e = repo.findById(idEqui);
        if(e.isPresent()){
            EquipeTechnicienDTO dto = mp.from(e.get());


            Set<TechnicienDTO> dtos = dto.getTechniciens();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        }

        return new ResponseEntity<>("Aucune equipe n'est trouve avec ce id : "+idEqui,HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<?> dissocierTechnicienFromEquipe(Long equipeId, Long technicienId) {
        EquipeIntervenant equipe = repo.findById(equipeId).orElse(null);
        Technicien technicien = trepo.findById(technicienId).orElse(null);

        if (equipe == null ) {
            return new ResponseEntity<>("Aucune equipe n'est trouve avec ce id : "+equipeId,HttpStatus.NOT_FOUND);
        }
        if(technicien==null){
            return new ResponseEntity<>("Aucune technicien n'est trouve avec ce id : "+technicienId,HttpStatus.NOT_FOUND);
        }



        equipe.getTechniciens().remove(technicien);
        technicien.getEquipes().remove(equipe);
        repo.save(equipe);

        return new ResponseEntity<>("Le technicien avec l'ID " + technicienId + " est supprimé de l'équipe.", HttpStatus.OK);
    }


    public ResponseEntity<?> getCompetenceOfEquipe(Long id) {
        Optional<EquipeIntervenant> equipeOptional = repo.findById(id);
        if (equipeOptional.isPresent()) {
            List<CompetenceDTO> competenceDTOs = new ArrayList<>();
            Set<Technicien> techniciens = equipeOptional.get().getTechniciens();
            for (Technicien technicien : techniciens) {
                Set<Competence> competences = technicien.getCompetences();
                for (Competence competence : competences) {
                    CompetenceDTO competenceDTO = new CompetenceDTO();
                    competenceDTO.setCompetence(competence.getCompetence()); // Assurez-vous d'adapter cela à votre modèle de données
                    competenceDTOs.add(competenceDTO);
                }
            }
            return new ResponseEntity<>(competenceDTOs, HttpStatus.OK);
        }
        return new ResponseEntity<>("Aucune équipe n'est trouvée avec cet ID : " + id, HttpStatus.NOT_FOUND);
    }
*/

}

