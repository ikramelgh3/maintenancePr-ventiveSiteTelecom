package net.elghz.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.userservice.enumeration.TypeTechnicien;
import net.elghz.userservice.model.Intervention;

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
    @Enumerated(EnumType.STRING)
    private TypeTechnicien type;

   /* @ManyToMany
    @JoinTable(
            name = "technicien_equipe",
            joinColumns = @JoinColumn(name = "technicien_id"),
            inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    private Set<EquipeIntervenant> equipes = new HashSet<>();*/
    @Transient
    List<Intervention> interventionList=new ArrayList<>();

    public boolean isDisponible() {
        return disponibilite;
    }
}
