package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.service.CTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class centreTechniqueController {


    @Autowired
    private CTService centreTechniqueService;

    @GetMapping("/CT/{id}")
    public CentreTechniqueDTO getCentreTechniqueById(@PathVariable Long id) {
        try {
            CentreTechniqueDTO centreTechniqueDTO = centreTechniqueService.getCatId(id);
            return centreTechniqueDTO;
        } catch (NotFoundException e) {
            return null;
        }
    }

    @PostMapping("/CT/exists")
    public ResponseEntity<Boolean> checkIfExists(@RequestBody Long centreTechniqueId) {
        boolean exists = centreTechniqueService.exists(centreTechniqueId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/CT/add-multiple")
    public ResponseEntity<List<CentreTechniqueDTO>> addMultipleCentreTechniques(@RequestBody List<CentreTechniqueDTO> centreTechniqueDTOList) {
        List<CentreTechniqueDTO> addedCentreTechniques = centreTechniqueService.addCentreTechniques(centreTechniqueDTOList);
        return ResponseEntity.ok().body(addedCentreTechniques);
    }
    @GetMapping("/CT/all")
    public List<CentreTechniqueDTO> getAllCentreTechniques() {
        List<CentreTechniqueDTO> centreTechniqueDTOs = centreTechniqueService.allCategories();
        return centreTechniqueDTOs;
    }

    @DeleteMapping("/CT/{id}")
    public ResponseEntity<String> deleteCentreTechniqueById(@PathVariable Long id) {
        boolean deleted = centreTechniqueService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Centre technique supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centre technique non trouvé avec l'ID: " + id);
        }
    }

    @PostMapping("/CT/add")
    public ResponseEntity<String> addCentreTechnique(@RequestBody CentreTechniqueDTO centreTechniqueDTO) {
        boolean added = centreTechniqueService.addCat(centreTechniqueDTO);
        if (added) {
            return ResponseEntity.ok("Centre technique ajouté avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Un centre technique avec ce nom existe déjà.");
        }
    }

    @PutMapping("/CT/update")
    public ResponseEntity<String> updateCentreTechnique(@RequestBody CentreTechniqueDTO updatedCentreTechnique) {
        boolean updated = centreTechniqueService.updateCat(updatedCentreTechnique);
        if (updated) {
            return ResponseEntity.ok("Centre technique mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centre technique non trouvé avec l'ID: " + updatedCentreTechnique.getId());
        }
    }

    @PutMapping("/ct/dc/{ctId}/{dcId}")
    public ResponseEntity<?> associateCTtoDC(@PathVariable Long ctId, @PathVariable Long dcId) {
        try {
            centreTechniqueService.associateCTtoDC(ctId, dcId);
            return ResponseEntity.ok("Centre technique associé avec succès à la DC.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/ct/dc/{ctId}")
    public ResponseEntity<?> disassociateCTfromDC(@PathVariable Long ctId) {
        try {
            centreTechniqueService.disassociateCTfromDC(ctId);
            return ResponseEntity.ok("Centre technique dissocié avec succès de la DC.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ct/bydc/{dcId}")
    public ResponseEntity<?> getCTsByDC(@PathVariable Long dcId) {
        try {
            List<CentreTechniqueDTO> ctList = centreTechniqueService.getCTsByDC(dcId);
            return ResponseEntity.ok(ctList);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/ct/dc/{ctId}")
    public ResponseEntity<?> getDCByCTId(@PathVariable Long ctId) {
        DC dc;
        try {
            String name = centreTechniqueService.getDCByCTId(ctId);
            if (name != null) {
                return ResponseEntity.ok(name);
            } else {
                return ResponseEntity.ok("Aucun DC associé à ce Centre Technique.");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}