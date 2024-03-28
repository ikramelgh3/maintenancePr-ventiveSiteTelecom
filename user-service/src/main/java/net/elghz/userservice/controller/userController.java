package net.elghz.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import net.elghz.userservice.repository.*;
import net.elghz.userservice.entities.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class userController {

    private userRepository repo;

    @GetMapping("/users")
    public List<utilisateur> users (){
        return repo.findAll();
    }
}
