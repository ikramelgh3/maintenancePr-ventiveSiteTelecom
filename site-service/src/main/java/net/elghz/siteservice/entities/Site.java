package net.elghz.siteservice.entities;

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
   /* @ManyToMany
    @JoinTable(name = "site_categorie",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_id"))
    private Set<categorie> categories = new HashSet<>();*/

    //@JsonManagedReference
    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(name = "site_activite" ,joinColumns = @JoinColumn(name = "site_id"),
    inverseJoinColumns = @JoinColumn(name = "activite_id"))
    private Set<TypeActivite> typeactivites = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)

    @JoinTable(name = "site_attribut",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "attribut_id"))
    private Set<Attribute> attributs = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL , mappedBy = "site")
    private List<equipement> equipements= new ArrayList<>();
    @ManyToOne @JoinColumn(name="CT_Id")
    private CentreTechnique centreTechnique;
    @OneToMany
            (mappedBy = "site" , cascade = CascadeType.ALL)
    private  List<Photo > photos = new ArrayList<>();

    public Site(SiteType siteType) {
    }/*

    public void addCategorie(categorie categorie) {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        this.categories.add(categorie);
    }
*/



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

    public void removeTypeActivite(TypeActivite typeActivite) {

        this.typeactivites.remove(typeActivite);
        typeActivite.removeSite(this);
    }

/*
    public void removeCategori(categorie e) {
        categories.remove(e);
        e.setSites(null);
    }*/
    public  void  addEquipement(equipement e){
        equipements.add(e);
    }

    public void removeEquipement(equipement e) {
        equipements.remove(e);
        e.setSite(null);
    }
    public void addAttribute(Attribute attribute) {
        if (this.attributs == null) {
            this.attributs = new HashSet<>();
        }
        this.attributs.add(attribute);

        attribute.getSites().add(this);
    }

    public void removeAttribute(Attribute attribute) {
        if (attributs != null) {
            attributs.remove(attribute);
            attribute.getSites().remove(this);
        }
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setSite(this);
    }

    public void ajouterImage(String imageName){
        this.photos.add(new Photo(imageName, this));
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        photo.setSite(null);
    }

}
