package net.elghz.siteservice.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import net.elghz.siteservice.entities.Site;

import java.util.HashSet;
import java.util.Set;
@Getter @Setter
public class typeActiviteDTO {

    private Long id;
    private String name;



    //private Set<siteDTO> sites = new HashSet<>();

}
