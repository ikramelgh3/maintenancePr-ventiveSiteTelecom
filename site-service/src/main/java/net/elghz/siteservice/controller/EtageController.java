package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.etageDTO;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.etage;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.service.DCService;
import net.elghz.siteservice.service.EtageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EtageController {
    @Autowired private EtageService dcService;

    @GetMapping("/etage/{id}")
    public ResponseEntity<?> getDCById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dcService.getCatId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/etage")
    public ResponseEntity<List<etageDTO>> getAllDCs() {
        return ResponseEntity.ok(dcService.allCategories());
    }

    @PostMapping("/etage/add")
    public ResponseEntity<?> addDC(@RequestBody etageDTO dcDTO) {
        if (dcService.addCat(dcDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Etage ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("L'etage existe déjà.");
        }
    }

    @PostMapping("/etage/add-multiple")
    public ResponseEntity<List<etage>> addMultipleDC(@RequestBody List<etageDTO> DCDTOList) {
        List<etage> addedCentreTechniques = dcService.addCentreTechniques(DCDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @PutMapping("/etage/{id}")
    public ResponseEntity<?> updateDC(@PathVariable Long id, @RequestBody etage updatedDCDTO) {
        updatedDCDTO.setId(id);
        if (dcService.updateCat(updatedDCDTO)) {
            return ResponseEntity.ok("Etage mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etage non trouvé.");
        }
    }

    @DeleteMapping("/etage/{id}")
    public ResponseEntity<?> deleteDC(@PathVariable Long id) {
        if (dcService.deleteById(id)) {
            return ResponseEntity.ok("L'etage est supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etage non trouvé.");
        }
    }

    @PutMapping("/etage/immuble/{dcId}/{drId}")
    public ResponseEntity<?> associateDCtoDR(@PathVariable Long dcId, @PathVariable Long drId) {
        try {
            dcService.associateEtagetoImmuble(dcId, drId);
            return ResponseEntity.ok("etage associé avec succès à l'immuble.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/etage/immuble/{dcId}")
    public ResponseEntity<?> disassociateDCfromDR(@PathVariable Long dcId) {
        try {
            dcService.disassociateCTfromDC(dcId);
            return ResponseEntity.ok("L'etage est dissocié avec succès de la DR.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/etageg/byImmuble/{drId}")
    public ResponseEntity<?> getDCsByDR(@PathVariable Long drId) {
        try {
            List<etageDTO> dcList = dcService.getEtageByImmuble(drId);
            return ResponseEntity.ok(dcList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}

