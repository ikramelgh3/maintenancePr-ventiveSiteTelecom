package net.elghz.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
public class TechnicienDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean disponibilite;
    private int nbreIntervetion;
    @OneToOne(cascade = CascadeType.ALL)
    private utilisateur utilisateur;
    @ManyToMany
    @JoinTable(
            name = "technician_competence",
            joinColumns = @JoinColumn(name = "technician_id"),
            inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    Set<Competence>  competences = new HashSet<>();
}
