package net.elghz.siteservice.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.categorieDTO;
import net.elghz.siteservice.exception.AttributeNotFoundException;
import net.elghz.siteservice.service.attributeService;
import net.elghz.siteservice.service.categorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class categorieController {
    private categorieService ser;

    @GetMapping("/categorie/{id}")
    public ResponseEntity<?> getAttrById(@PathVariable Long id) {
        try {
            Optional<categorieDTO> dtoOptional = ser.getCatId(id);
            return new ResponseEntity<>(dtoOptional.orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun Categorie avec cet ID : " + id)),
                    HttpStatus.OK);
        } catch (AttributeNotFoundException ex) {
            return new ResponseEntity<>("Aucun attribut avec cet ID : " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/categorie/all")
    public ResponseEntity<?> getAttributs(){
        List <categorieDTO> a= ser.allCategories();
        if(!a.isEmpty()){
            return new ResponseEntity<>(a , HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("La liste des catégories est vide ", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/catagorie/add")
    public ResponseEntity<String> addAttribute(@RequestBody categorieDTO attribute) {
        boolean added = ser.addCat(attribute);
        if (added) {
            return new ResponseEntity<>("La categorie est bien ajouté", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("La categorie avec ce nom est déja existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/categorie/{id}")
    public ResponseEntity<String> updateAttribute(@PathVariable Long id, @RequestBody categorieDTO updatedAttribute) {
        updatedAttribute.setId(id);
        boolean updated = ser.updateCat(updatedAttribute);
        if (updated) {
            return new ResponseEntity<>("La categorie est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun categorie n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/categorie/{id}")
    public ResponseEntity<String> deleteAttribute(@PathVariable Long id) {
        boolean deleted = ser.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>("La categorie est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun categorie n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{categorieId}/addAttribute/{attributeId}")
    public ResponseEntity<String> addAttributeToCategorie(@PathVariable Long categorieId, @PathVariable Long attributeId) {
        ser.addAttributeToCategorie(categorieId, attributeId);
        return ResponseEntity.ok("Attribut ajouté avec succès à la catégorie.");
    }

    @DeleteMapping("/{categorieId}/removeAttribute/{attributeId}")
    public ResponseEntity<String> removeAttributeFromCategorie(@PathVariable Long categorieId, @PathVariable Long attributeId) {
        ser.removeAttributeFromCategorie(categorieId, attributeId);
        return ResponseEntity.ok("Attribut supprimé avec succès de la catégorie.");
    }

    @PostMapping("/{categoryId}/attributes/{attributeId}")
    public ResponseEntity<String> ajouterAttributACategorie(@PathVariable Long categoryId, @PathVariable Long attributeId) {
        try {
            ser.ajouterAttributACategorie(categoryId, attributeId);
            return ResponseEntity.ok("L'attribut a été ajouté à la catégorie avec succès.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}/attributes/{attributeId}")
    public ResponseEntity<String> dissocierAttributDeCategorie(@PathVariable Long categoryId, @PathVariable Long attributeId) {
        try {
            ser.dissocierAttributDeCategorie(categoryId, attributeId);
            return ResponseEntity.ok("L'attribut a été dissocié de la catégorie avec succès.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

}
