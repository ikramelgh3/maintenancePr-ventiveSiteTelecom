package net.elghz.userservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import net.elghz.userservice.model.ChecklistDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class ResponsableMaintenance extends  utilisateur{
    @Transient
    private List<ChecklistDTO> checklist = new ArrayList<>();
}
