package net.elghz.interventionservice.model;

import jakarta.persistence.*;
import lombok.Data;
import net.elghz.interventionservice.enumeration.PlanningStatus;

import java.util.Date;
@Data
public class Planning {

    private Long id;
    private String name;
    private Date dateDebutRealisation;
    private Date dateFinRealisation;
    private String semestre;
    private PlanningStatus status;

}
