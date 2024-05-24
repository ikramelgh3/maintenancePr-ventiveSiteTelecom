package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.repository.DRRepo;
import net.elghz.siteservice.service.DCService;
import net.elghz.siteservice.service.DRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DRController {
    @Autowired private DRService dRService;

@Autowired private DRRepo repo;

    @GetMapping("/Dr/{id}")
    public DR getDRById(@PathVariable Long id) {
        return  repo.findById(id).get();
    }
    @GetMapping("/DR/{id}")
    public ResponseEntity<?> getDCById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dRService.getCatId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/DR")
    public List<DRDTO> getAllDCs() {
        return dRService.allCategories();
    }

    @PostMapping("/DR/add")
    public ResponseEntity<?> addDR(@RequestBody DRDTO dcDTO) {
        if (dRService.addCat(dcDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("DR ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Le DR existe déjà.");
        }
    }

    @PostMapping("/DR/add-multiple")
    public ResponseEntity<List<DRDTO>> addMultipleDC(@RequestBody List<DRDTO> DRDTOList) {
        List<DRDTO> addedCentreTechniques = dRService.addCentreTechniques(DRDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @PutMapping("/DR/{id}")
    public ResponseEntity<?> updateDC(@PathVariable Long id, @RequestBody DRDTO updatedDCDTO) {
        updatedDCDTO.setId(id);
        if (dRService.updateCat(updatedDCDTO)) {
            return ResponseEntity.ok("DR mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DR non trouvé.");
        }
    }

    @DeleteMapping("/DR/{id}")
    public ResponseEntity<?> deleteDC(@PathVariable Long id) {
        if (dRService.deleteById(id)) {
            return ResponseEntity.ok("DR supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DR non trouvé.");
        }
    }
}

