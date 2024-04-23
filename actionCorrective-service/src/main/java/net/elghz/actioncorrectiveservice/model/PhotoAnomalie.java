package net.elghz.actioncorrectiveservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.actioncorrectiveservice.enumeration.MomentPrisephoto;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PhotoAnomalie {

    private Long id;
    private String name;
    private MomentPrisephoto moment;
  //  private Anomalie anomalie;


}
