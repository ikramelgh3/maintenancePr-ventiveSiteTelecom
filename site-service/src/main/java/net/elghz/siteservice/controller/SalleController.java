package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.salleDTO;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.etage;
import net.elghz.siteservice.entities.salle;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.service.DCService;
import net.elghz.siteservice.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SalleController {
    @Autowired private SalleService dcService;

    @GetMapping("/salle/{id}")
    public ResponseEntity<?> getDCById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dcService.getCatId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/salle")
    public ResponseEntity<List<salleDTO>> getAllDCs() {
        return ResponseEntity.ok(dcService.allCategories());
    }

    @PostMapping("/salle/add")
    public ResponseEntity<?> addDC(@RequestBody salleDTO dcDTO) {
        if (dcService.addCat(dcDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("La salle ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("La salle existe déjà.");
        }
    }

    @PostMapping("/salle/add-multiple")
    public ResponseEntity<List<salle>> addMultipleDC(@RequestBody List<salleDTO> DCDTOList) {
        List<salle> addedCentreTechniques = dcService.addCentreTechniques(DCDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @PutMapping("/salle/{id}")
    public ResponseEntity<?> updateDC(@PathVariable Long id, @RequestBody salle updatedDCDTO) {
        updatedDCDTO.setId(id);
        if (dcService.updateCat(updatedDCDTO)) {
            return ResponseEntity.ok("La salle mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salle non trouvé.");
        }
    }

    @DeleteMapping("/salle/{id}")
    public ResponseEntity<?> deleteDC(@PathVariable Long id) {
        if (dcService.deleteById(id)) {
            return ResponseEntity.ok("La salle est supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Salle non trouvé.");
        }
    }

    @PutMapping("/salle/etage/{dcId}/{drId}")
    public ResponseEntity<?> associateDCtoDR(@PathVariable Long dcId, @PathVariable Long drId) {
        try {
            dcService.associateCTtoDC(dcId, drId);
            return ResponseEntity.ok("La salle est associée avec succès à l'étage donné.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/salle/etage/{dcId}")
    public ResponseEntity<?> disassociateDCfromDR(@PathVariable Long dcId) {
        try {
            dcService.disassociateCTfromDC(dcId);
            return ResponseEntity.ok("La salle est dissociée avec succès de la DR.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/salle/etage/{drId}")
    public ResponseEntity<?> getDCsByDR(@PathVariable Long drId) {
        try {
            List<salleDTO> dcList = dcService.getCTsByDC(drId);
            return ResponseEntity.ok(dcList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @GetMapping ("/salle/id/{name}")
    public etage findIDByName(@PathVariable int name){
        return dcService.getDCFromCT(name);
    }

    @PutMapping("/equi/salle/{dcId}/{drId}")
    public ResponseEntity<?> ajouterEquipementSalle(@PathVariable Long dcId, @PathVariable Long drId) {
        try {
            dcService.addEquipementToSalle(dcId, drId);
            return ResponseEntity.ok("L'équipement est bien ajouté à la salle donnée.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/equi/salle/{eq}/{idS}")
    public ResponseEntity<?>supprimerEquipementFromSalle(@PathVariable Long eq, @PathVariable Long idS) {
        try {
            dcService.supprimerEquipementFromSalle(eq,idS);
            return ResponseEntity.ok("L'équipement est supprimé de la salle.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

