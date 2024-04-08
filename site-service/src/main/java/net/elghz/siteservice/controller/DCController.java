package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.service.DCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DCController {
    @Autowired private  DCService dcService;

    @GetMapping("/DC/{id}")
    public ResponseEntity<?> getDCById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dcService.getCatId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all/DC")
    public ResponseEntity<List<DCDTO>> getAllDCs() {
        return ResponseEntity.ok(dcService.allCategories());
    }

    @PostMapping("/DC/add")
    public ResponseEntity<?> addDC(@RequestBody DCDTO dcDTO) {
        if (dcService.addCat(dcDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("DC ajouté avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Le DC existe déjà.");
        }
    }

    @PostMapping("/DC/add-multiple")
    public ResponseEntity<List<DCDTO>> addMultipleDC(@RequestBody List<DCDTO> DCDTOList) {
        List<DCDTO> addedCentreTechniques = dcService.addCentreTechniques(DCDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @PutMapping("/DC/{id}")
    public ResponseEntity<?> updateDC(@PathVariable Long id, @RequestBody DCDTO updatedDCDTO) {
        updatedDCDTO.setId(id);
        if (dcService.updateCat(updatedDCDTO)) {
            return ResponseEntity.ok("DC mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DC non trouvé.");
        }
    }

    @DeleteMapping("/DC/{id}")
    public ResponseEntity<?> deleteDC(@PathVariable Long id) {
        if (dcService.deleteById(id)) {
            return ResponseEntity.ok("DC supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DC non trouvé.");
        }
    }

    @PutMapping("/dc/dr/{dcId}/{drId}")
    public ResponseEntity<?> associateDCtoDR(@PathVariable Long dcId, @PathVariable Long drId) {
        try {
            dcService.associateDCtoDR(dcId, drId);
            return ResponseEntity.ok("DC associé avec succès à la DR.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/dc/dr/{dcId}")
    public ResponseEntity<?> disassociateDCfromDR(@PathVariable Long dcId) {
        try {
            dcService.disassociateDCfromDR(dcId);
            return ResponseEntity.ok("DC dissocié avec succès de la DR.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/dc/bydr/{drId}")
    public ResponseEntity<?> getDCsByDR(@PathVariable Long drId) {
        try {
            List<DC> dcList = dcService.getDCsByDR(drId);
            return ResponseEntity.ok(dcList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/dc/dr/{dcId}")
    public ResponseEntity<?> getDRByDCId(@PathVariable Long dcId) {

        try {
            String name = dcService.getDRByDC(dcId);
            if (name != null) {
                return ResponseEntity.ok(name);
            } else {
                return ResponseEntity.ok("Aucun DR associé à ce DC.");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping ("/dc/name/{name}")
    public Long findIDByName(@PathVariable String name){
        return dcService.DCIdByNamr(name);
    }

}

