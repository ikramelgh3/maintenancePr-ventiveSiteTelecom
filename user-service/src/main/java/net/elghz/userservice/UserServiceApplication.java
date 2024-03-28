package net.elghz.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import  net.elghz.userservice.repository.*;
import net.elghz.userservice.entities.*;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner ( userRepository repo){

        return args -> {
            List<utilisateur> users = List.of(utilisateur.builder().nom("elgh").build(),utilisateur.builder().nom("agj").build());
            repo.saveAll(users);
        };
    }
}
