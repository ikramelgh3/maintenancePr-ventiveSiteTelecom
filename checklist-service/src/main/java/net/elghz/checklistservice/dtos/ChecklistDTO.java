package net.elghz.checklistservice.dtos;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import net.elghz.checklistservice.entities.PointMesure;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ChecklistDTO {

    private Set<PointMesureDTO> measurementPoints = new HashSet<>();

    private Long id;

}
