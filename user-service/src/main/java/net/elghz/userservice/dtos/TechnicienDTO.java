package net.elghz.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.userservice.entities.Competence;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
public class TechnicienDTO extends utilisateurDTO{


    private Boolean disponibilite;
    private int nbreIntervetion;
    private int niveau;

    //private net.elghz.userservice.entities.utilisateur utilisateur;

    Set<CompetenceDTO>  competences = new HashSet<>();


}
