package net.elghz.interventionservice.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointMesureDTO {
    private Long id;
    private String attribut;

    private List<String> resultatsPossibles;

    //private Set<checklist> checklists = new HashSet<>();

    //private List<Resultat> resultats;

    private Long typeEquipementId;

    private typeEquipement typeEquipent;
}
