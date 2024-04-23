package net.elghz.interventionservice.model;

import lombok.Data;
import net.elghz.interventionservice.enumeration.TypeTechnicien;

import java.util.HashSet;
import java.util.Set;
@Data
public class TechnicienDTO {
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
