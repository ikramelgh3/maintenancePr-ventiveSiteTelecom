package net.elghz.userservice.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChecklistDTO {
    private Long id;
    private String description;


    private List<pointMesure> pointMesures = new ArrayList<>();

    // Getters and setters
}
