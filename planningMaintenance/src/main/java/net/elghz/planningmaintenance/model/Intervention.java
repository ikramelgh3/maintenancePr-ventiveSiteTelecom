package net.elghz.planningmaintenance.model;

import lombok.Data;
import net.elghz.planningmaintenance.enumeration.statusIntervention;

import java.util.Date;
@Data
public class Intervention {
    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;

    private String description;

    private statusIntervention status;
}
