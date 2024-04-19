package net.elghz.siteservice.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.siteservice.entities.PhotoEquipement;
import net.elghz.siteservice.model.ChecklistDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class equipementDTO {
    private Long id;
    private String numeroSerie;
    private String nom;
    private String descreption;
    private String type;
    private String marque;
    private String statut;
    private Date dateMiseService;
    private Date dateMiseHorsService;
    //private List<ChecklistDTO> checklistDTOS = new ArrayList<>();

    private List<PhotoEquipementDTO> photoEquipements = new ArrayList<>();
}
