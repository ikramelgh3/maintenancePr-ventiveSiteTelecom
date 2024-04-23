package net.elghz.anomalieservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.anomalieservice.entities.PhotoAnomalie;
import net.elghz.anomalieservice.enumeration.Gravite;
import net.elghz.anomalieservice.enumeration.StatusGravite;
import net.elghz.anomalieservice.model.ActionCorrective;
import net.elghz.anomalieservice.model.Interevntion;
import net.elghz.anomalieservice.model.Technicien;

import java.util.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AnomalieDTO {

    private Long id;
    private String titre;
    private String descreption;
    private Gravite gravite;
    private StatusGravite status;
    private Date dateDetection;
    private  Date dateResolution;
    private Long id_TechnicienD;
    private Technicien technicien;
    private Long id_TechnicienR;
    private Technicien technicienResolvant;

    private Long id_Intervention;

    private Interevntion interevntion;


    private Set<PhotoAnomalieDTO> photos = new HashSet<>();
    List<ActionCorrective> actionsCorrectives = new ArrayList<>();
   /* public void addPhoto(PhotoAnomalieDTO photo) {
        photos.add(photo);
        photo.setAnomalie(this);
    }

    public void removePhoto(PhotoAnomalieDTO photo) {
        photos.remove(photo);
        photo.setAnomalie(null);
    }*/




}
