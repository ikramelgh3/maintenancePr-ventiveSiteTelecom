package net.elghz.userservice.service;


import net.elghz.userservice.entities.Role;
import net.elghz.userservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class roleService {

    @Autowired private RoleRepository repo;


    public ResponseEntity<?> addRo(Role ptDto){

        repo.save(ptDto);
        return new ResponseEntity<>("Le role est bien ajouté" , HttpStatus.OK);
    }
    public ResponseEntity<?> addR(List <Role> ptDto){
        List<Role> pointMesures = new ArrayList<>();
         for ( Role p :ptDto){

            pointMesures.add(p);
         }

        repo.saveAll(pointMesures);
        return new ResponseEntity<>("Les roles sont bien ajoutés" , HttpStatus.OK);
    }
    public ResponseEntity<?> findById( Long id){
        Optional<Role> ch = repo.findById(id);

        if(ch.isPresent()){

            return new ResponseEntity<>(ch, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune role n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity <?> deleteR( Long id)
    {
        Optional<Role> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("Le role est supprimé", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune role n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }
/*
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
*/
    public ResponseEntity<?> allR(){

       List<Role> checklists= repo.findAll();

       if(checklists.size()!=0){
            return new ResponseEntity<>(checklists, HttpStatus.OK);}
       else{
           return new ResponseEntity<>("Aucune role n'est trouvé", HttpStatus.NOT_FOUND);
       }
    }




}
