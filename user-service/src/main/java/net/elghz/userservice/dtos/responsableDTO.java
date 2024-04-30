package net.elghz.userservice.dtos;

import lombok.Data;
import net.elghz.userservice.model.ChecklistDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class responsableDTO extends utilisateurDTO {

    private List<ChecklistDTO> checklist = new ArrayList<>();
}
