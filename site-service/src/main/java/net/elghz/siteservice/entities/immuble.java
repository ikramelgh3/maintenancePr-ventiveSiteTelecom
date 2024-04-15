package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class immuble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String addr;
    @OneToMany (cascade = CascadeType.ALL , mappedBy = "immuble")
    private List<etage> etageList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

}
