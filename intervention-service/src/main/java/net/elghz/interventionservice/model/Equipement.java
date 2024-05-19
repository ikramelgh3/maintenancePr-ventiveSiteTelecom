package net.elghz.interventionservice.model;

import lombok.Data;
import net.elghz.interventionservice.enumeration.Statut;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class Equipement {


    private Long id;
    private String numeroSerie;
    private String nom;
    private String descreption;
    private String type;
    private String marque;
    private Statut statut;
    private Date dateMiseService;
    private Date dateMiseHorsService;
    private List<PhotoEquipementDTO> photoEquipements = new ArrayList<>();
    private salleDTO salle;
    private typeEquipementDTO typeEquipementt;
}
