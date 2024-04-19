package net.elghz.userservice.service;


import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.mapper.mapper;
import net.elghz.userservice.repository.CompetenceRepository;
import net.elghz.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class competenceService {

    @Autowired private CompetenceRepository repo;

    @Autowired private mapper mp;

    public ResponseEntity<?> addRo(CompetenceDTO ptDto){


        Competence c = mp.from(ptDto);
       Competence cc = repo .findByCompetence(c.getCompetence());
       if (cc == null){
           repo.save(c);
           return new ResponseEntity<>("La competence est bien ajoutée" , HttpStatus.OK);
       }
       else {

           return new ResponseEntity<>("La competence existe déja" , HttpStatus.OK);
       }

    }
    public ResponseEntity<?> addR(List <CompetenceDTO> ptDto){
        List<Competence> pointMesures = new ArrayList<>();
         for ( CompetenceDTO p :ptDto){

             Competence c = mp.from(p);

            pointMesures.add(c);
         }

        repo.saveAll(pointMesures);
        return new ResponseEntity<>("Les competences sont bien ajoutés" , HttpStatus.OK);
    }
    public ResponseEntity<?> findById( Long id){
        Optional<Competence> ch = repo.findById(id);

        if(ch.isPresent()){
        CompetenceDTO c = mp.from(ch.get());

            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune competence n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <?> deleteR( Long id)
    {
        Optional<Competence> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("La competence est supprimé", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune competence n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updatePT(Long id){
        Optional<Competence> ch = repo.findById(id);

        if(ch.isPresent()){
            CompetenceDTO dto = mp.from(ch.get());
            mp.updateCompetence(dto, ch.get());
            return new ResponseEntity<>("La competence est bien modifiée", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune competence n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> allR(){

       List<Competence> checklists= repo.findAll();
        List<CompetenceDTO> competenceDTOS = new ArrayList<>();
       if(checklists.size()!=0){
           for (Competence c : checklists){
               CompetenceDTO  d= mp.from(c);
               competenceDTOS.add(d);
           }
            return new ResponseEntity<>(competenceDTOS, HttpStatus.OK);}
       else{
           return new ResponseEntity<>("Aucune competence n'est trouvé", HttpStatus.NOT_FOUND);
       }
    }





}
