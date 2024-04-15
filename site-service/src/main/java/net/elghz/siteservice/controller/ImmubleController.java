package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.etageDTO;
import net.elghz.siteservice.dtos.immubleDTO;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.immuble;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.service.ImmubleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ImmubleController {
    @Autowired private ImmubleService dcService;

    @GetMapping("/Immuble/{id}")
    public ResponseEntity<?> getDCById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dcService.getCatId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/Immuble")
    public ResponseEntity<List<immubleDTO>> getAllDCs() {
        return ResponseEntity.ok(dcService.allCategories());
    }

    @PostMapping("/Immuble/add")
    public ResponseEntity<?> addDC(@RequestBody immubleDTO dcDTO) {
        if (dcService.addCat(dcDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("L'immuble ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("L'immuble existe déjà.");
        }
    }

    @PostMapping("/Immuble/add-multiple")
    public ResponseEntity<List<immuble>> addMultipleDC(@RequestBody List<immubleDTO> DCDTOList) {
        List<immuble> addedCentreTechniques = dcService.addImmuble(DCDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @PutMapping("/Immuble/{id}")
    public ResponseEntity<?> updateDC(@PathVariable Long id, @RequestBody immuble updatedDCDTO) {
        updatedDCDTO.setId(id);
        if (dcService.updateCat(updatedDCDTO)) {
            return ResponseEntity.ok("l'immuble mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("immuble non trouvé.");
        }
    }

    @DeleteMapping("/Immuble/{id}")
    public ResponseEntity<?> deleteDC(@PathVariable Long id) {
        if (dcService.deleteById(id)) {
            return ResponseEntity.ok("L'immuble supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'immuble non trouvé.");
        }
    }

    @PutMapping("/immuble/site/{dcId}/{drId}")
    public ResponseEntity<?> associateDCtoDR(@PathVariable Long dcId, @PathVariable Long drId) {
        try {
            dcService.associateImmubletoSite(dcId, drId);
            return ResponseEntity.ok("Immuble associé avec succès au site donnee.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @DeleteMapping("/immuble/site/{dcId}")
    public ResponseEntity<?> disassociateDCfromDR(@PathVariable Long dcId) {
        try {
            dcService.disassociateImmublefromSite(dcId);
            return ResponseEntity.ok("L'immuble dissocié avec succès de la DR.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/immuble/bySite/{drId}")
    public ResponseEntity<?> getDCsByDR(@PathVariable Long drId) {
        try {
            List<immubleDTO> dcList = dcService.getCTsBySite(drId);
            return ResponseEntity.ok(dcList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping ("/site/name/{name}")
    public Long findIDByName(@PathVariable String name){
        return dcService. getDCFromCT(name).getId();
    }

}

