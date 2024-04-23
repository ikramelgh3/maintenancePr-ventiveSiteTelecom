package net.elghz.interventionservice.model;

import jakarta.persistence.*;
import net.elghz.interventionservice.enumeration.Gravite;
import net.elghz.interventionservice.enumeration.StatusGravite;

import java.util.Date;

public class Anomalie {
    private Long id;
    private String titre;
    private String descreption;
    private Gravite gravite;
    private StatusGravite status;
    private Date dateDetection;
    private  Date dateResolution;

    private Long id_Technicien;

    private TechnicienDTO technicien;


}
