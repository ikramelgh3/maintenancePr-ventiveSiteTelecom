package net.elghz.checklistservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class typeEquipement {
    private Long id;
    private String name;

}
