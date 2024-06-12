package net.elghz.checklistservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.Resultat;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointMesureDTO {
    private Long id;
    private String attribut;

    private List<String> resultatsPossibles;

    //private Set<ChecklistDTO> checklists = new HashSet<>();

    private List<Resultat> resultats;

    private Long typeEquipementId;

    private typeEquipement typeEquipent;
}
