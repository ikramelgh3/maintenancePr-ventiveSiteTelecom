package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    @OneToMany (mappedBy ="dc" , cascade = CascadeType.ALL)
    private List<CentreTechnique> centreTechniqueList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="dr_id")
    private DR dr;
}
