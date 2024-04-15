package net.elghz.userservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.userservice.service.userService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import net.elghz.userservice.repository.*;
import net.elghz.userservice.entities.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class userController {

    private userRepository repo;
    private userService ser;

    @GetMapping("/users")
    public List<utilisateur> users (){
        return ser.users();
    }

    @PutMapping("/user/{username}/role/{rolename}")
    public ResponseEntity<?> associerRoleToUser(@PathVariable String username , @PathVariable String rolename){
        ser.addRoleToUser(username, rolename);
        return  new ResponseEntity<>("Le role est bien associe à l'utilisateur" , HttpStatus.OK);
    }
}
