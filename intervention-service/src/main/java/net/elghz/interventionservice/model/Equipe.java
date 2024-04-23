package net.elghz.interventionservice.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class Equipe {

    private Long id;

    private int nbreTechnicien;

    private Set<TechnicienDTO> techniciens = new HashSet<>();
}
