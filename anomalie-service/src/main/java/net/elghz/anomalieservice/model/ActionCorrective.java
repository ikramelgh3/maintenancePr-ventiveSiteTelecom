package net.elghz.anomalieservice.model;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;
import net.elghz.anomalieservice.entities.Anomalie;

import java.util.Date;

@Data
public class ActionCorrective {
    private Long id;
    private String description;
    private Date dateRealisation;
    private Long idAnomalie;
    private Anomalie anomalie;
}
