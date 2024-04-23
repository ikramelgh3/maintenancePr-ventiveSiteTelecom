package net.elghz.anomalieservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.anomalieservice.enumeration.MomentPrisephoto;
import net.elghz.anomalieservice.enumeration.StatusGravite;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhotoAnomalie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MomentPrisephoto moment;
    @ManyToOne
    @JoinColumn(name = "anomalie_id")
    private Anomalie anomalie;

    public PhotoAnomalie(String fileName, Anomalie anomalie, MomentPrisephoto momentPrisephoto) {
        this.name= fileName;

        this.anomalie=anomalie;
        this.moment=momentPrisephoto;
    }
}
