package net.elghz.userservice.controller;

import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.service.competenceService;
import net.elghz.userservice.service.roleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class competenceController {

    @Autowired
    competenceService ser;
    @GetMapping("/competence/all")
    public ResponseEntity<?> findAll(){
         return ser.allR();
    }
    @PostMapping ("/competence/add")
    public ResponseEntity<?> addRole(@RequestBody CompetenceDTO ch){
        return  ser.addRo(ch);
    }

    @PostMapping ("/competences/add")
    public ResponseEntity<?> addRoles(@RequestBody List<CompetenceDTO> ch){
        return  ser.addR(ch);
    }
    @GetMapping("/competence/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
         return  ser.findById(id);
    }
    @DeleteMapping("/competence/delete/{id}")
    public ResponseEntity<?> deleteR(@PathVariable Long id){
         return  ser.deleteR(id);
    }


}
