package net.elghz.interventionservice.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class technicien {
    private String nom;

    private String username;

    private Boolean disponibilite;
    private int nbreIntervetion;
    //Set<CompetenceDTO> competences = new HashSet<>();
}
