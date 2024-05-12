package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.dtos.SiteFixeDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.model.Planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE" , length = 6)
public  class Site {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private String code;
    @Column(unique = true)
    private String name;
    private String typeSite;
    private Double latitude;
    private Double longitude;
    private String addresse;
    private String typeInstallation;
    private String typeAlimentation;
    private String typeTransmission;
    private Boolean presenceGESecours;
    @Transient
    private  List<Planning> plannings = new ArrayList<>();

    @OneToMany
            (mappedBy = "site" , cascade = CascadeType.ALL)
    private List<immuble> immubles = new ArrayList<>();

    @ManyToMany (cascade = CascadeType.ALL)
    @JoinTable(name = "site_activite" ,joinColumns = @JoinColumn(name = "site_id"),
    inverseJoinColumns = @JoinColumn(name = "activite_id"))
    private Set<TypeActivite> typeactivites = new HashSet<>();
   /* @ManyToMany(cascade = CascadeType.ALL)

    @JoinTable(name = "site_attribut",
            joinColumns = @JoinColumn(name = "site_id"),
            inverseJoinColumns = @JoinColumn(name = "attribut_id"))
    private Set<Attribute> attributs = new HashSet<>();
*/

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

    /*
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
*/
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


    public void update(siteDTO dto) {
        this.setName(dto.getName());
        this.setAddresse(dto.getAddresse());
        this.setCode(dto.getCode());
       //this.setCentreTechnique(dto.getCentreTechnique());
        this.setLatitude(dto.getLatitude());
        this.setLongitude(dto.getLongitude());
        this.setPresenceGESecours(dto.getPresenceGESecours());
        this.setTypeInstallation(dto.getTypeInstallation());
        this.setTypeAlimentation(dto.getTypeAlimentation());

    }

    @Transient
    public List<String> getPhotosImagePaths() {
        List<String> imagePaths = new ArrayList<>();
        String basePath = "site-service/site-images/" + this.id;

        for (Photo photo : this.photos) {
            String imagePath = basePath + "/" + photo.getName();
            imagePaths.add(imagePath);
        }

        return imagePaths;
    }

}
