package net.elghz.userservice.model;

import net.elghz.userservice.enumeration.PlanningStatus;

import java.util.Date;

public class Planning {

    private Long id;
    private String name;
    private Date dateDebutRealisation;
    private Date dateFinRealisation;
    private String semestre;
    private PlanningStatus status;
}
