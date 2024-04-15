package net.elghz.checklistservice.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
public class ChecklistDTO {

    private  Long id;
    private String description;

    private List<PointMesureDTO> pointMesures = new ArrayList<>();
}
