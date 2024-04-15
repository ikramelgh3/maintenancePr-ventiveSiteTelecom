package net.elghz.siteservice.dtos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import net.elghz.siteservice.entities.Site;

@Data
public class SiteMobileDTO extends siteDTO {

    private String supportAntennes;
    private Double hauteurSupportAntenne;
    private String lieuInsatallationBTS;


    public void update ( SiteMobileDTO sm ){
        this.setHauteurSupportAntenne(sm.getHauteurSupportAntenne());
        this.setSupportAntennes(sm.getSupportAntennes());
        this.setLieuInsatallationBTS(sm.getLieuInsatallationBTS());
    }


}
