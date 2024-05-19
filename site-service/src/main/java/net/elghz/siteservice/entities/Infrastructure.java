package net.elghz.siteservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Infrastructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeInstaallation;
    private String supportAntennes;
    private String hauteurSupportAntenne;
    private String lieuInstallationBTS;


}
