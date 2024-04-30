package net.elghz.userservice.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.dtos.addTechnicien;
import net.elghz.userservice.dtos.utilisateurDTO;
import net.elghz.userservice.service.userService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.elghz.userservice.repository.*;
import net.elghz.userservice.entities.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController

@AllArgsConstructor

public class userController {

    private userRepository repo;
    @Autowired private userService ser;

    @GetMapping("/users")
    public List<utilisateurDTO> users (){
        return ser.users();
    }

    @PutMapping("/user/{username}/role/{rolename}")
    public ResponseEntity<?> associerRoleToUser(@PathVariable String username , @PathVariable String rolename){
        ser.addRoleToUser(username, rolename);
        return  new ResponseEntity<>("Le role est bien associe à l'utilisateur" , HttpStatus.OK);
    }
/*
    @PostMapping("/add/technicien")
    public ResponseEntity<?> addTechnicien (@RequestBody addTechnicien request){
        return  ser.addTechnicen(request.getU(),request.getDetailsDTO(),request.getCompetenceDTO() );
    }*/

    /*
    @GetMapping("/technicien/{username}")
    public ResponseEntity<?> findTechnicienByUsername(@PathVariable String username){
        return ser.getTechnicienDetails(username);
    }

    @GetMapping("/all/technicien")
    public ResponseEntity<?> allTechnicein(){
        return ser.getAllTechniciensDetails();
    }


    @GetMapping("/gettechnicien/{id}")
    public ResponseEntity<?> recupererTech(@PathVariable Long id){
        return ser.recupererTechn(id);
    }
    @PostMapping("/{technicienId}/competences")
    public ResponseEntity<String> assignerCompetences(@PathVariable Long technicienId, @RequestBody List<Long> competenceIds) {
        try {
            ser.assignerCompetences(technicienId, competenceIds);
            return ResponseEntity.ok("Compétences assignées avec succès au technicien avec l'ID : " + technicienId);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }*/
}
