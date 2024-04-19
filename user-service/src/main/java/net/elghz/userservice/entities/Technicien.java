package net.elghz.userservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Technicien extends utilisateur{
    private Boolean disponibilite;
    private int nombreIntervention;
    private int niveau;
    @ManyToMany
    @JoinTable(
            name = "technicien_competence",
            joinColumns = @JoinColumn(name = "technicien_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    Set<Competence> competences = new HashSet<>();
}
