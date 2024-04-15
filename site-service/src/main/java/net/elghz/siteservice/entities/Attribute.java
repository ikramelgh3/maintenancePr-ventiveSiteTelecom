package net.elghz.siteservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.dtos.attributeDTO;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
@Entity
public class Attribute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String attributeValue;
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private categorie categorie;

    //@ManyToMany(mappedBy = "attributs")
    //private Set<Site> sites = new HashSet<>();

    public Attribute(String attributeName, String attributeValue) {
        this.attributeValue=attributeValue;
        this.name=attributeName;
    }
}
