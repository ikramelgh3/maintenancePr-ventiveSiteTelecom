package net.elghz.siteservice.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FIXE")
public class SiteFixe extends  Site {
    public SiteFixe() {
        // Initialisation par défaut si nécessaire
    }
}