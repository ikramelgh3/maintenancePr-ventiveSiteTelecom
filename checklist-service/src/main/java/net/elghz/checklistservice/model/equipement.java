package net.elghz.checklistservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class equipement {
    private Long id;
    private String numeroSerie;
    private String nom;
    private String descreption;
    private String type;
    private String marque;
    private String statut;
    private Date dateMiseService;
    private Date dateMiseHorsService;
    private typeEquipement typeEquipementt;

    private List<PhotoEquipementDTO> photoEquipements = new ArrayList<>();
}
