package net.elghz.planningmaintenance.dto;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;

import java.util.Date;
@Data
public class PlanningMaintenanceDTO {
    private Long id;
    private String name;
    private Date dateDebutRealisation;
    private Date dateFinRealisation;
    private String semestre;

    private PlanningStatus status;

}
