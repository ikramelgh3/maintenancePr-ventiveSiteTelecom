package net.elghz.checklistservice.dtos;

import jakarta.persistence.Transient;
import lombok.*;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;

import java.util.ArrayList;
import java.util.List;
@Data
public class ChecklistDTO {

    private  Long id;
    private String description;

    private long respo_ID;

    private ResponsableMaintenance respoMaint;
    private List<PointMesureDTO> pointMesures = new ArrayList<>();
    private equipement equi ;
    private Long equipement_id;
}
