package net.elghz.anomalieservice.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import net.elghz.anomalieservice.enumeration.MomentPrisephoto;
@Data
public class piecejointes {
    private Long id ;
    private String name;
    private MomentPrisephoto moment_prise_photo;
}
