package net.elghz.checklistservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointMesureDTO {

    private Long id;
     private String attribut;
    private String value;

    //private Checklist checklist;
}
