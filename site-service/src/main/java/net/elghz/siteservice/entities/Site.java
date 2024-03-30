package net.elghz.siteservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.enumeration.SiteType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
@Entity
public class Site {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private SiteType type;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attribute> attributes ;
   // @JsonManagedReference
    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(name = "site_activite" ,joinColumns = @JoinColumn(name = "site_id"),
    inverseJoinColumns = @JoinColumn(name = "activite_id"))
    private Set<TypeActivite> typeactivites = new HashSet<>();

    public Site(SiteType type, List<Attribute> attributes, Set<TypeActivite> activites) {
        this.type = type;
        this.attributes = attributes;
        this.typeactivites =activites;
    }

    public Site(SiteType siteType) {
    }

    public void addAttribute(Attribute attribute) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attribute);
    }

    public void addTypeActivite(TypeActivite typeActivite) {
        if (this.typeactivites == null) {
            this.typeactivites = new HashSet<>();
        }
        this.typeactivites.add(typeActivite);

        typeActivite.getSites().add(this);
    }

    public void clearTypeActivites (){
        this.typeactivites.clear();

    }
}
