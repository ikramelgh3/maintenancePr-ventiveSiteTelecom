package net.elghz.interventionservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.model.*;

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

    private String description;

    @Enumerated(EnumType.STRING)
    private statusIntervention status;

    private Long id_Planning;
    @Transient
    private Planning planning;

    private Long id_Techn;
    @Transient
    private TechnicienDTO technicien;

    private Long id_Equipement;
    @Transient
    private Equipement equipement;

    @Transient
    List<Anomalie> anomaliesDetected = new ArrayList<>();
}
