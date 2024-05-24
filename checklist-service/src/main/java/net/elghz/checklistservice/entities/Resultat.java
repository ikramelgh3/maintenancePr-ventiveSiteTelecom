package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Resultat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point_de_mesure_id")
    private PointMesure pointDeMesure;

    private String resultat;
    private Double valeur;

    private String commentaire;

   // private String technicien;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInspection;
}
