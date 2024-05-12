package net.elghz.interventionservice.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class salleDTO {

    private Long id;

    private String codeSalle;
    private int numeroSalle;
    private int capacity;
    private etage etage;
}
