package net.elghz.planningmaintenance.model;

import lombok.Data;
import net.elghz.planningmaintenance.enumeration.statusIntervention;

import java.time.LocalTime;
import java.util.Date;
@Data
public class Intervention {
    private Long id;
    private String name;
    private Date dateDebut;

    private Date dateFin;
    private LocalTime heureDebut;
    //private PrioriteEnum priorite;

    private ResponsableMaint responsable;
    private String description;
    private Long id_Planning;

   // private Planning planning;
   // private TypeIntervention type;
    private statusIntervention status;
    private String id_Techn;

    private ResponsableMaint technicien;

    private Long id_Equipement;
   // private Equipement equipement;
}
