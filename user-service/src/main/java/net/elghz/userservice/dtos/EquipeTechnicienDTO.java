package net.elghz.userservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.userservice.entities.Technicien;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EquipeTechnicienDTO {

    private Long id;

    private int nbreTechnicien;

    private Set<TechnicienDTO> techniciens = new HashSet<>();
}
