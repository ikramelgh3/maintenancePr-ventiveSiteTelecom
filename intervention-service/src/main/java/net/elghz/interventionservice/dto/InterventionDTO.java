package net.elghz.interventionservice.dto;

import jakarta.persistence.*;
import lombok.Data;
import net.elghz.interventionservice.enumeration.PrioriteEnum;
import net.elghz.interventionservice.enumeration.TypeIntervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.model.Equipe;
import net.elghz.interventionservice.model.Equipement;
import net.elghz.interventionservice.model.Planning;
import net.elghz.interventionservice.model.TechnicienDTO;

import java.time.LocalTime;
import java.util.Date;
@Data
public class InterventionDTO {
    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;
    private LocalTime heureDebut;
    private PrioriteEnum priorite;

    private TechnicienDTO responsable;
    private String description;
    private Long id_Planning;

    private Planning planning;
    private TypeIntervention type;
    private statusIntervention status;
    private String id_Techn;

    private TechnicienDTO technicien;

    private Long id_Equipement;
    private Equipement equipement;
}
