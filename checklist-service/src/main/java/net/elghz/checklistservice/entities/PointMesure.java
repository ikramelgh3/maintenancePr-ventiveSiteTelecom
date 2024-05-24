package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;

import java.util.List;


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

    @OneToMany(mappedBy = "pointDeMesure", cascade = CascadeType.ALL)
    private List<Resultat> resultats;
    private Long respo_Id;
    @Transient
    private ResponsableMaintenance respoMaint;
    private Long typeEquipementId;
    @Transient
    private typeEquipement typeEquipent;
}
