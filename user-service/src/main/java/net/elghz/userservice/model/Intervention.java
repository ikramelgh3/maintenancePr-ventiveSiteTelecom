package net.elghz.userservice.model;

import net.elghz.userservice.enumeration.statusIntervention;

import java.util.Date;

public class Intervention {

    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;

    private String description;

    private statusIntervention status;
}
