package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.SiteType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public    class siteDTO {


    private  Long id;

    private String code;

    private String typeSite;
    private String name;
    private Double latitude;
    private Double longitude;
    private String addresse;

    private String typeInstallation;
    private String typeAlimentation;
    private String typeTransmission;
    private Boolean presenceGESecours;


    private List<immubleDTO> immubles = new ArrayList<>();


    private Set<typeActiviteDTO> typeactivites = new HashSet<>();



    private CentreTechniqueDTO centreTechnique;

    private  List<PhotoDTO > photos = new ArrayList<>();



    public void update(siteDTO dto) {
        this.setName(dto.getName());
        this.setAddresse(dto.getAddresse());
        this.setCode(dto.getCode());
        this.setCentreTechnique(dto.getCentreTechnique());
        this.setLatitude(dto.getLatitude());
        this.setLongitude(dto.getLongitude());
        this.setPresenceGESecours(dto.getPresenceGESecours());
        this.setTypeInstallation(dto.getTypeInstallation());
        this.setTypeAlimentation(dto.getTypeAlimentation());

    }
}