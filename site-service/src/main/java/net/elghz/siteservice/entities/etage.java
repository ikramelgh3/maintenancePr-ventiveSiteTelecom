package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class etage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private int numeroEtage;
    private String codeEtage;
    private int nbreSalle;
    @Column(unique = true)
    private String codeEtagge;

    @OneToMany (mappedBy = "etage" , cascade = CascadeType.ALL)
    private List<salle> salles = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "immuble_id")
    private immuble immuble;


}
