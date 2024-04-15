package net.elghz.userservice.service;

import lombok.AllArgsConstructor;
import net.elghz.userservice.entities.Role;
import net.elghz.userservice.entities.utilisateur;
import net.elghz.userservice.repository.RoleRepository;
import net.elghz.userservice.repository.userRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Utilities;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class userService {
    private userRepository repo;
    private RoleRepository rrepo;

    public utilisateur addNewUser( utilisateur user){
        return repo.save(user);
    }

    public Role addNewRole( Role role){
        return  rrepo.save(role);
    }

    public void addRoleToUser(String username, String rolename){
        utilisateur optUser = repo.findByUsername(username);
        Role r = rrepo.findByRoleName(rolename);
        optUser.getRoles().add(r);

    }
    public utilisateur loadUser( String username){
        return repo.findByUsername(username);
    }

    public List<utilisateur> users (){
         return repo.findAll();
    }
}
