package net.elghz.siteservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ChecklistDTO {
    private Long id;
    private String description;


    private List<pointMesure> pointMesures = new ArrayList<>();
}
