package net.elghz.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.userservice.entities.utilisateur;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class addTechnicienDTO {
    private utilisateurDTO u;
    //private TechnicienDetailsDTO detailsDTO;
    //private List<CompetenceDTO> competenceDTO;
}
