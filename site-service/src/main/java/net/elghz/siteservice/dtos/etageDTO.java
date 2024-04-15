package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.siteservice.entities.salle;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class etageDTO {

    private  Long id;
    private int numeroEtage;

    private List<salleDTO> salles = new ArrayList<>();

}
