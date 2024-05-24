package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;

import java.util.ArrayList;
import java.util.List;


//@Entity
public class Checklist {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private  Long id;
//    private String description;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checklist")
//    private List<PointMesure> pointMesures = new ArrayList<>();
//
//    private Long respo_Id;
//    @Transient
//    private ResponsableMaintenance respoMaint;
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
