package net.elghz.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import net.elghz.userservice.model.Intervention;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor

public class EquipeIntervenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int nbreTechnicien;
/*

    @ManyToMany(mappedBy = "equipes")
    private Set<Technicien> techniciens = new HashSet<>();
    @Transient
    List<Intervention> interventions = new ArrayList<>();

    public void addTechnicien(Technicien technicien) {

        techniciens.add(technicien);

    }*/
}
