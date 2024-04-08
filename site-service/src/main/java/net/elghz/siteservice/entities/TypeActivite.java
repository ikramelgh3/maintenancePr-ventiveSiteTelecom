package net.elghz.siteservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TypeActivite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "typeactivites")

    private Set<Site> sites = new HashSet<>();


    public void addSite(Site site) {
        sites.add(site);
        site.getTypeactivites().add(this);
    }
    public void removeSite(Site site) {
        sites.remove(site);
        site.getTypeactivites().remove(this);
    }

    public void clearSites (){
        this.sites.clear();

    }
}
