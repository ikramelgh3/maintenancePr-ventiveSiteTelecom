package net.elghz.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import net.elghz.userservice.entities.Role;

import java.util.ArrayList;
import java.util.Collection;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class utilisateurDTO {

    private Long id;
    private String nom;

    private String username;

   private String password;

    private Collection<Role> roles = new ArrayList<>();
}
