package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(unique = true)
    private String nom;

    private String descreption;

    private String type;
    private String marque;
    private String statut;
    private Date dateMiseService;
    private Date dateMiseHorsService;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;
    @OneToMany(mappedBy = "equipement" , cascade = CascadeType.ALL)
    private List<PhotoEquipement> photoEquipements = new ArrayList<>();

    public void addPhoto(PhotoEquipement photo) {
        photoEquipements.add(photo);
        photo.setEquipement(this);
    }

    public void removePhoto(PhotoEquipement photo) {
        photoEquipements.remove(photo);
        photo.setEquipement(null);
    }

}
