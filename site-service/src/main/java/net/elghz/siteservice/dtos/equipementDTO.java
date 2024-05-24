package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.entities.PhotoEquipement;
import net.elghz.siteservice.entities.salle;
import net.elghz.siteservice.entities.typeEquipement;
import net.elghz.siteservice.enumeration.Statut;
import net.elghz.siteservice.model.ChecklistDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class equipementDTO {
    private Long id;
    private String numeroSerie;
    private String nom;
    private String code;
    private String descreption;
    private Statut statut;
    private String marque;
    private Date dateMiseService;
    private Date dateMiseHorsService;
    private typeEquipementDTO   typeEquipementt;
    private List<ChecklistDTO> checklistDTOS = new ArrayList<>();
    private List<PhotoEquipementDTO> photoEquipements = new ArrayList<>();
    private salleDTO salle;
}
