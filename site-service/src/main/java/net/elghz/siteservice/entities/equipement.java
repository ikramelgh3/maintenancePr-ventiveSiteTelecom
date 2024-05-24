package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.siteservice.enumeration.Statut;
import net.elghz.siteservice.model.ChecklistDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String numeroSerie;
    private String nom;
    @Column(unique = true)
    private String code;
    private String descreption;
    @Enumerated(EnumType.STRING)
    private Statut statut;
    private String marque;
    private Date dateMiseService;
    private Date dateMiseHorsService;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private typeEquipement   typeEquipementt;
    @Transient
    private List<ChecklistDTO> checklistDTOS = new ArrayList<>();
    @OneToMany(mappedBy = "equipement" , cascade = CascadeType.ALL)
    private List<PhotoEquipement> photoEquipements = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "salle_id")
    private salle salle;
    public void addPhoto(PhotoEquipement photo) {
        photoEquipements.add(photo);
        photo.setEquipement(this);
    }

    public void removePhoto(PhotoEquipement photo) {
        photoEquipements.remove(photo);
        photo.setEquipement(null);
    }

}
