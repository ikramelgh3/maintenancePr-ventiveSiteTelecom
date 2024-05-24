
package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "centre_technique", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class CentreTechnique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne @JoinColumn(name = "dc_id")
    private DC dc;
    @OneToMany
            (mappedBy = "centreTechnique" )
    List<Site> sites = new ArrayList<>();


}
