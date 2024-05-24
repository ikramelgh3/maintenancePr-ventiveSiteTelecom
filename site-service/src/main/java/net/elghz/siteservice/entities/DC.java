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
@Table(name = "dc", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class DC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "dc", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CentreTechnique> centreTechniqueList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="dr_id")
    private DR dr;
}
