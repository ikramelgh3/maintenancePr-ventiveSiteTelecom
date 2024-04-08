package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CentreTechnique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    @ManyToOne @JoinColumn(name = "dc_id")
    private DC dc;
    @OneToMany
            (mappedBy = "centreTechnique" , cascade = CascadeType.ALL)
    List<Site> sites = new ArrayList<>();


}
