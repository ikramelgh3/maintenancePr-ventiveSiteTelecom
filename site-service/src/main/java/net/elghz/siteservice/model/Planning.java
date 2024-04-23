package net.elghz.siteservice.model;

import jakarta.persistence.*;
import net.elghz.siteservice.enumeration.PlanningStatus;

import java.util.Date;

public class Planning {

    private Long id;
    private String name;
    private Date dateDebutRealisation;
    private Date dateFinRealisation;
    private String semestre;
    private PlanningStatus status;
}
