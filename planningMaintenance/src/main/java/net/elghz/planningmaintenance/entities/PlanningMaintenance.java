package net.elghz.planningmaintenance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;

import java.util.Date;
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

}
