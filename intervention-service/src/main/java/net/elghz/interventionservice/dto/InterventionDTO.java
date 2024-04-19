package net.elghz.interventionservice.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import net.elghz.interventionservice.enumeration.statusIntervention;

import java.util.Date;
@Data
public class InterventionDTO {
    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;

    private String description;

    private statusIntervention status;
}
