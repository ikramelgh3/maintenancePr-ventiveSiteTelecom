package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhotoEquipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] picByte;
    private Date dateAjout;
    @ManyToOne @JoinColumn(name = "equipement_id")
    private equipement equipement;


    public PhotoEquipement(String name,equipement equipement) {
        this.name = name;
        this.equipement = equipement;
    }
}
