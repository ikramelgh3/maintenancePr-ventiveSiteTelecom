package net.elghz.siteservice.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.siteservice.dtos.SiteMobileDTO;
import net.elghz.siteservice.dtos.siteDTO;

@Entity
@DiscriminatorValue("MOBILE")
@Getter @Setter
public class SiteMobile  extends Site {
    public SiteMobile() {
        // Initialisation par défaut si nécessaire
    }

    private String supportAntennes;
    private Double hauteurSupportAntenne;
    private String lieuInsatallationBTS;

    public void update ( SiteMobileDTO sm ){
        this.setHauteurSupportAntenne(sm.getHauteurSupportAntenne());
        this.setSupportAntennes(sm.getSupportAntennes());
        this.setLieuInsatallationBTS(sm.getLieuInsatallationBTS());
    }

}