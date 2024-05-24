package net.elghz.siteservice.controller;

import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.importFile.ImporterSite;
import net.elghz.siteservice.importFile.importerCentre;
import net.elghz.siteservice.repository.CTRepo;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.DRRepo;
import net.elghz.siteservice.repository.SiteRepository;
import net.elghz.siteservice.service.CTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class centreTechniqueController {

@Autowired
CTRepo repo;
    @Autowired
    private CTService centreTechniqueService;
    @Autowired
    private importerCentre importeCentre;

    @Autowired
    DCRepo dcRepo;
    @Autowired
    DRRepo drRepo;
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

    @DeleteMapping("/CT")
    public void deleteCentreTechnique(@RequestBody CentreTechnique id) {
        centreTechniqueService.delete(id);

    }
    @DeleteMapping("/CT/{id}")
    public void deleteCentreTechniqueById(@PathVariable Long id) {
        centreTechniqueService.deleteById(id);

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

    @PostMapping("/import-ct")
    public List<CentreTechniqueDTO> importSites(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }


        try {
            return   importeCentre.importCentreTechniques(file.getInputStream() );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();

        } catch (DataIntegrityViolationException ex) {
            return new ArrayList<>();

        }
    }



    @PutMapping("/update/centre/{id}")
    public CentreTechniqueDTO updateCt(@PathVariable Long id , @RequestBody CentreTechnique c){
         return  centreTechniqueService.updateCentreTechnique(id, c);
    }

    @GetMapping("/exists/{name}/{dcId}/{drId}")
    public boolean checkCentreTechniqueExists(
            @PathVariable String name,
            @PathVariable Long dcId,
            @PathVariable Long drId) {
        return centreTechniqueService.checkIfCentreTechniqueExists(name, dcId, drId);
    }



    @GetMapping("/existCT/{name}")
    public Boolean checkSiteExistsCode( @PathVariable String name) {
        boolean exists = repo.existsByName(name);
        return exists;
    }


    @PostMapping ("/add/ct/{idDC}")
     public  void addCT( @RequestBody CentreTechnique c, @PathVariable Long idDC ){
        DC dc = dcRepo.findById(idDC).get();
        c.setDc( dc);
        repo.save(c);

    }

}