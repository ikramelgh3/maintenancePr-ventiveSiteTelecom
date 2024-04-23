package net.elghz.anomalieservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.anomalieservice.enumeration.Gravite;
import net.elghz.anomalieservice.enumeration.StatusGravite;
import net.elghz.anomalieservice.model.ActionCorrective;
import net.elghz.anomalieservice.model.Interevntion;
import net.elghz.anomalieservice.model.Technicien;

import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Anomalie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titre;
    private String descreption;
    @Enumerated(EnumType.STRING)
    private Gravite gravite;
    @Enumerated(EnumType.STRING)
    private StatusGravite status;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_detection")
    private Date dateDetection;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_resolution")
    private  Date dateResolution;

    private Long id_TechnicienD;
    @Transient
    private Technicien technicienDetecteur;

    private Long id_TechnicienR;
    @Transient
    private Technicien technicienResolvant;


    private Long id_Intervention;
    @Transient
    private Interevntion interevntion;

    @OneToMany(mappedBy = "anomalie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhotoAnomalie> photos = new HashSet<>();

    @Transient
    List<ActionCorrective> actionsCorrectives = new ArrayList<>();
    public void addPhoto(PhotoAnomalie photo) {
        photos.add(photo);
        photo.setAnomalie(this);
    }

    public void removeActionCorrective(ActionCorrective act ){
        actionsCorrectives.remove(act);
        act.setAnomalie(null);
    }
    public void removePhoto(PhotoAnomalie photo) {
        photos.remove(photo);
        photo.setAnomalie(null);
    }




}
