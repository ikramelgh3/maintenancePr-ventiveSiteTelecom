package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Checklist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long typeEquipementId;
    @Transient
    private typeEquipement typeEquipent;
    @ManyToMany
    @JoinTable(
            name = "checklist_measurementpoint",
            joinColumns = @JoinColumn(name = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "measurementpoint_id")
    )
    private Set<PointMesure> measurementPoints = new HashSet<>();



//    private Long equipement_id;
//    @Transient
//    private equipement equi;
//
//    public void addPointMesure(PointMesure p) {
//        pointMesures.add(p);
//        p.setChecklist(this);
//    }
//
//    public void removePointMesure(PointMesure p) {
//        pointMesures.remove(p);
//        p.setChecklist(null);
//    }

}
