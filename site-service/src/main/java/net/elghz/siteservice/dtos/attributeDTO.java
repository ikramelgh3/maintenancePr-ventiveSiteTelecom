package net.elghz.siteservice.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.categorie;
import net.elghz.siteservice.enumeration.AttributeCategory;

import java.util.HashSet;
import java.util.Set;

@Data
public class attributeDTO {

    private Long id;
    private String name;
    private String attributeValue;

    //private AttributeCategory attributeCategory;
    //@JsonBackReference
    private categorieDTO categorie;
    //private Set<siteDTO> sites = new HashSet<>();
}
