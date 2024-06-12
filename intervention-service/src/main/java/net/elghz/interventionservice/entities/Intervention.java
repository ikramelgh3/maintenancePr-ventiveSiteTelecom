package net.elghz.interventionservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.interventionservice.enumeration.PrioriteEnum;
import net.elghz.interventionservice.enumeration.TypeIntervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.model.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    private LocalTime heureDebut;
    private String description;
    @Enumerated(EnumType.STRING)

    private TypeIntervention type;

    @Enumerated(EnumType.STRING)

    private PrioriteEnum priorite;
    @Enumerated(EnumType.STRING)
    private statusIntervention status;

    private Long id_Planning;
    @Transient
    private Planning planning;

    private String id_Techn;
    @Transient
    private TechnicienDTO technicien;


    private String id_Respo;
    @Transient
    private TechnicienDTO responsable;

    private Long id_Equipement;
    @Transient
    private Equipement equipement;

    @Transient
    List<Anomalie> anomaliesDetected = new ArrayList<>();
}
