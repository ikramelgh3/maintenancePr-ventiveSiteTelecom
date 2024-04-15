package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.etage;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class immubleDTO {

    private Long id;
    private String name;
    private String addr;

    private List<etageDTO> etageList = new ArrayList<>();

    //private Site site;

}
