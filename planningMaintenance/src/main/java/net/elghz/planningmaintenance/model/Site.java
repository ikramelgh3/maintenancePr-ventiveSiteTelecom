package net.elghz.planningmaintenance.model;

import lombok.Data;

@Data
public class Site {

    private Long id;
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
    private String supportAntennes;
    private Double hauteurSupportAntenne;
    private String lieuInsatallationBTS;

}

