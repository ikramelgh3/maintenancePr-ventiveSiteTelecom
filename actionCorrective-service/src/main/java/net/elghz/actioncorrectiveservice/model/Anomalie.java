package net.elghz.actioncorrectiveservice.model;

import jakarta.persistence.*;
import lombok.Data;
import net.elghz.actioncorrectiveservice.enumeration.Gravite;
import net.elghz.actioncorrectiveservice.enumeration.StatusGravite;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
public class Anomalie {

    private Long id;

    private String titre;
    private String descreption;

    private Gravite gravite;

    private StatusGravite status;

    private Date dateDetection;

    private  Date dateResolution;

    private Long id_TechnicienD;

    private Technicien technicienDetecteur;

    private Long id_TechnicienR;

    private Technicien technicienResolvant;


    private Long id_Intervention;

    private Interevntion interevntion;

    private Set<PhotoAnomalie> photos = new HashSet<>();



}
