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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class checklistService {

    @Autowired private checklistRepo repo;
    @Autowired private pointMesureRepo prepo;
    @Autowired private mapper mp;

    public ResponseEntity<?> addChecklist(ChecklistDTO checklist){
        Checklist ch = mp.from(checklist);
        repo.save(ch);
        return new ResponseEntity<>("Checklist bien ajoutée" , HttpStatus.OK);
    }
    public ResponseEntity<?> findById( Long id){
        Optional<Checklist> ch = repo.findById(id);

        if(ch.isPresent()){
            ChecklistDTO dto = mp.from(ch.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune checklist n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <?> deleteChecklist( Long id)
    {
        Optional<Checklist> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("Checklist supprimée", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune checklist n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateCHeck(Long id){
        Optional<Checklist> ch = repo.findById(id);

        if(ch.isPresent()){
            ChecklistDTO dto = mp.from(ch.get());
            mp.update(dto, ch.get());
            return new ResponseEntity<>("Checklist est bien modifie", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune checklist n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> allChecklist(){

       List<Checklist> checklists= repo.findAll();
       List<ChecklistDTO> dto = checklists.stream().map(mp::from).collect(Collectors.toList());
       if(dto.size()!=0){
            return new ResponseEntity<>(dto, HttpStatus.OK);}
       else{
           return new ResponseEntity<>("Aucune checklist n'est trouvé", HttpStatus.NOT_FOUND);
       }
    }

    public ResponseEntity<?> ajouterPointMesureToChecklist(Long id , PointMesureDTO pointMesures){

        Optional<Checklist> ch = repo.findById(id);
        if( ch.isPresent()){

            Checklist chl = ch.get();

                PointMesure pp = mp.from(pointMesures);
                chl.addPointMesure(pp);

            repo.save(chl);
            return new ResponseEntity<>("Le point de mesures est bien ajouté à la checklist", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Aucune checklist n'est trouvé", HttpStatus.NOT_FOUND);
        }

    }
    public ResponseEntity<?> ajouterPointMesuresToChecklist(Long id , List<PointMesureDTO> pointMesures){

        Optional<Checklist> ch = repo.findById(id);
        if( ch.isPresent()){

            Checklist chl = ch.get();
            for( PointMesureDTO p : pointMesures){
                PointMesure pp = mp.from(p);
                chl.addPointMesure(pp);
            }
            repo.save(chl);
            return new ResponseEntity<>("Les points de mesures sont bien ajoutés à la checklist", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Aucune checklist n'est trouvé", HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<?> addPointMesuresToChecklist(Long id, List<Long> idP) {
        Optional<Checklist> optionalChecklist = repo.findById(id);

        if (optionalChecklist.isPresent()) {
            Checklist checklist = optionalChecklist.get();

            for (Long pointId : idP) {
                Optional<PointMesure> optionalPointMesure = prepo.findById(pointId);

                if (optionalPointMesure.isPresent()) {
                    PointMesure pointMesure = optionalPointMesure.get();
                    checklist.addPointMesure(pointMesure);
                } else {
                    return new ResponseEntity<>("Aucun point de mesure n'a été trouvé avec cet ID : " + pointId, HttpStatus.NOT_FOUND);
                }
            }

            repo.save(checklist);
            return new ResponseEntity<>("Les points de mesure ont été ajoutés avec succès à la checklist.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune checklist trouvée avec cet ID : " + id, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> supprimerPointMesureToChecklist(Long id , PointMesureDTO pointMesures){

        Optional<Checklist> ch = repo.findById(id);
        if( ch.isPresent()){

            Checklist chl = ch.get();

                PointMesure pp = mp.from(pointMesures);
                chl.removePointMesure(pp);

            repo.save(chl);
            return new ResponseEntity<>("Point de mesure est bien supprimé de la checklist", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Aucune checklist n'est trouvé", HttpStatus.NOT_FOUND);
        }

    }


    public ResponseEntity<?> ajouterChecklistComplet(ChecklistDTO checklistDTO) {
        // Convertir le DTO en entité Checklist
        Checklist ch = mp.from(checklistDTO);

        // Récupérer les points de mesure de la checklist
        List<PointMesure> pointsMesure = ch.getPointMesures();

        // Boucler à travers les points de mesure
        for (PointMesure p : pointsMesure) {
            // Définir la checklist pour chaque point de mesure
            p.setChecklist(ch);
        }

        // Sauvegarder la checklist avec ses points de mesure associés
        repo.save(ch);

        // Vous n'avez pas besoin de sauvegarder les points de mesure individuellement car ils sont déjà associés à la checklist

        return new ResponseEntity<>("La checklist est bien ajoutée", HttpStatus.OK);
    }


}
