package net.elghz.checklistservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointMesure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     private String attribut;
    private String value;
    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;
}
