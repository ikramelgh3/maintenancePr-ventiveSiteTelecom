package net.elghz.actioncorrectiveservice.model;

import lombok.Data;
import net.elghz.actioncorrectiveservice.enumeration.*;

import java.util.Date;

@Data
public class Interevntion {

    private Long id;
    private String name;

    private Date dateDebut;
    private Date dateFin;

    private String description;


    private statusIntervention status;
}
