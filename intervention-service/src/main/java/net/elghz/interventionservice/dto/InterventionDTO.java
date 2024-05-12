package net.elghz.interventionservice.dto;

import jakarta.persistence.*;
import lombok.Data;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.model.Equipe;
import net.elghz.interventionservice.model.Equipement;
import net.elghz.interventionservice.model.TechnicienDTO;

import java.util.Date;
@Data
public class InterventionDTO {
    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;

    private String description;

    private statusIntervention status;
    private Long id_Techn;

    private TechnicienDTO technicien;

    private Long id_Equipement;
    private Equipement equipement;
}
