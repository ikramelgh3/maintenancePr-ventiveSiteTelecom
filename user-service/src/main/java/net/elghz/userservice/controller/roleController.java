package net.elghz.userservice.controller;

import net.elghz.userservice.entities.Role;
import net.elghz.userservice.service.roleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class roleController {

    @Autowired
    roleService ser;
    @GetMapping("/role/all")
    public ResponseEntity<?> findAll(){
         return ser.allR();
    }
    @PostMapping ("/role/add")
    public ResponseEntity<?> addRole(@RequestBody Role ch){
        return  ser.addRo(ch);
    }

    @PostMapping ("/roles/add")
    public ResponseEntity<?> addRoles(@RequestBody List<Role> ch){
        return  ser.addR(ch);
    }
    @GetMapping("/role/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
         return  ser.findById(id);
    }
    @DeleteMapping("/role/delete/{id}")
    public ResponseEntity<?> deleteR(@PathVariable Long id){
         return  ser.deleteR(id);
    }


}
