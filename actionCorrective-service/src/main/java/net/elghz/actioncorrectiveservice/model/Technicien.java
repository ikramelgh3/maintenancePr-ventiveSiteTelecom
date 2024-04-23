package net.elghz.actioncorrectiveservice.model;

import lombok.Data;
import net.elghz.actioncorrectiveservice.enumeration.*;

import java.util.HashSet;
import java.util.Set;

@Data
public class Technicien {

    private String nom;
    private String username;
    private String email;
    private String telephone;
    private Boolean disponibilite;
    private int nbreIntervetion;
    private int niveau;
    Set<CompetenceDTO> competences = new HashSet<>();
    private TypeTechnicien type;
}
