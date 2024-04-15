package net.elghz.checklistservice.services;

import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import net.elghz.checklistservice.mapper.mapper;
import net.elghz.checklistservice.repository.checklistRepo;
import net.elghz.checklistservice.repository.pointMesureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class pointMesureService {

    @Autowired private pointMesureRepo repo;
    @Autowired private mapper mp;

    public ResponseEntity<?> addPT(PointMesureDTO ptDto){
        PointMesure p = mp.from(ptDto);
        repo.save(p);
        return new ResponseEntity<>("Point de mesure bien ajouté" , HttpStatus.OK);
    }
    public ResponseEntity<?> addPTS(List <PointMesureDTO> ptDto){
        List<PointMesure> pointMesures = new ArrayList<>();
         for ( PointMesureDTO p :ptDto){
             PointMesure pp = mp.from(p);
            pointMesures.add(pp);
         }

        repo.saveAll(pointMesures);
        return new ResponseEntity<>("Les points de mesure sont bien ajoutés" , HttpStatus.OK);
    }
    public ResponseEntity<?> findById( Long id){
        Optional<PointMesure> ch = repo.findById(id);

        if(ch.isPresent()){
            PointMesureDTO dto = mp.from(ch.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune point de mesure n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <?> deletePT( Long id)
    {
        Optional<PointMesure> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("Point de mesure supprimée", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune point de mesure n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updatePT(Long id){
        Optional<PointMesure> ch = repo.findById(id);

        if(ch.isPresent()){
            PointMesureDTO dto = mp.from(ch.get());
            mp.updatePointMesure(dto, ch.get());
            return new ResponseEntity<>("Point de mesure est bien modifie", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune point de mesure n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> allPTS(){

       List<PointMesure> checklists= repo.findAll();
        List<PointMesureDTO> dto =  checklists.stream().map(mp::from).collect(Collectors.toList());
       if(dto.size()!=0){
            return new ResponseEntity<>(dto, HttpStatus.OK);}
       else{
           return new ResponseEntity<>("Aucune point de mesure n'est trouvé", HttpStatus.NOT_FOUND);
       }
    }




}
