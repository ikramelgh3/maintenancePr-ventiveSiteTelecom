package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointMesure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     private String attribut;
    @ElementCollection
    @CollectionTable(name = "resultats_possibles", joinColumns = @JoinColumn(name = "point_de_mesure_id"))
    @Column(name = "resultat")
    private List<String> resultatsPossibles;
    @ManyToMany(mappedBy = "measurementPoints")
    private Set<Checklist> checklists = new HashSet<>();
    @OneToMany(mappedBy = "pointDeMesure", cascade = CascadeType.ALL)
    private List<Resultat> resultats;

    private Long typeEquipementId;
    @Transient
    private typeEquipement typeEquipent;
}
