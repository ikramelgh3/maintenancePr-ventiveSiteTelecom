package net.elghz.siteservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.service.attributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class attributeController {
    private attributeService ser;

    @GetMapping("/attribute/id/{id}")
    public ResponseEntity<?> getAttributeId(@PathVariable Long id){
        Optional<Attribute> a = ser.getAttributeId(id);
        if(a.isPresent()){
            return new ResponseEntity<>(a , HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("Aucun attribut n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/attribute/all")
    public ResponseEntity<?> getAttributs(){
        List <Attribute> a= ser.allAttribute();
        if(!a.isEmpty()){
            return new ResponseEntity<>(a , HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("La liste des attributes est vide ", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/attribute/add")
    public ResponseEntity<String> addAttribute(@RequestBody Attribute attribute) {
        boolean added = ser.addAttribute(attribute);
        if (added) {
            return new ResponseEntity<>("L'attribut est bien ajouté", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Lattribut avec ce nom est déja existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/attribute/{id}")
    public ResponseEntity<String> updateAttribute(@PathVariable Long id, @RequestBody Attribute updatedAttribute) {
        updatedAttribute.setId(id);
        boolean updated = ser.updateAttribute(updatedAttribute);
        if (updated) {
            return new ResponseEntity<>("L'attribut est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun attribut n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/attribute/{id}")
    public ResponseEntity<String> deleteAttribute(@PathVariable Long id) {
        boolean deleted = ser.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>("L'attribut est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun attribut n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

}
