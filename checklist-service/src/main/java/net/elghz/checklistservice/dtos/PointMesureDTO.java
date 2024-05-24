package net.elghz.checklistservice.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.elghz.checklistservice.entities.Resultat;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PointMesureDTO {
    private Long id;
    private String attribut;
    private List<String> resultatsPossibles;
  //  private List<Resultat> resultats;
    private Long respo_Id;
    private ResponsableMaintenance respoMaint;
    private typeEquipement typeEquipent;
    private Long typeEquipementId;
}
