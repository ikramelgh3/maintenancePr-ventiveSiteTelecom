package net.elghz.anomalieservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.enumeration.MomentPrisephoto;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhotoAnomalieDTO {

    private Long id;
    private String name;
    private MomentPrisephoto moment;
   // private AnomalieDTO anomalie;

}
