package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.siteservice.entities.equipement;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class salleDTO {

    private Long id;
    private int numeroSalle;
    private int capacity;

    private etageDTO etage; // Référence à l'étage associé

}