package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.SiteType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter @Setter
public class siteDTO {
    private  Long id;

    private String name;

    private SiteType type;

    private Set<typeActiviteDTO> typeactivites = new HashSet<>();

    private Set<attributeDTO> attributs = new HashSet<>();
    private List<equipementDTO> equipements= new ArrayList<>();

    private CentreTechniqueDTO centreTechnique;

    private  List<PhotoDTO> photos = new ArrayList<>();
}
