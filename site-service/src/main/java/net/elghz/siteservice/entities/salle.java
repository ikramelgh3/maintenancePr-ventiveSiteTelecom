package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numeroSalle;
   private int capacity;

    @ManyToOne
    @JoinColumn(name = "etage_id")
    private etage etage;



    @OneToMany(mappedBy = "salle" , cascade = CascadeType.ALL)
    private List<equipement> equipementList = new ArrayList<>();

    public void addEquipement(equipement eqq) {
        equipementList.add(eqq);
        eqq.setSalle(this);
    }

    public void removePhoto(equipement photo) {
        equipementList.remove(photo);
        photo.setSalle(null);
    }


}
