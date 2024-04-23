package net.elghz.actioncorrectiveservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.actioncorrectiveservice.model.Anomalie;

import java.util.Date;
@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ActionCorrective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRealisation;

    private Long idAnomalie;
    @Transient
    private Anomalie anomalie;
}
