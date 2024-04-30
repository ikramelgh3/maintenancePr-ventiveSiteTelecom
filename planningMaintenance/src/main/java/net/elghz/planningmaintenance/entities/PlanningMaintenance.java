package net.elghz.planningmaintenance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.model.ResponsableMaint;
import net.elghz.planningmaintenance.model.Site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PlanningMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "debut_realisation")
    private Date dateDebutRealisation;
    @Temporal(TemporalType.DATE)
    @Column(name = "fin_realisation")
    private Date dateFinRealisation;
    private String semestre;
    @Enumerated(EnumType.STRING)
    private PlanningStatus status;
    private String description;
    private Date dateCreation;
    private Long id_Site;
    @Transient
    private Site site;
    private Long id_Respo;
    @Transient
    private ResponsableMaint responsableMaint;
    @Transient
    List<Intervention> interventionList = new ArrayList<>();

}
