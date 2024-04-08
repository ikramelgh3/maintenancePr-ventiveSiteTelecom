package net.elghz.gestionparam.controller;

import lombok.AllArgsConstructor;
import net.elghz.gestionparam.entities.parametrage;
import net.elghz.gestionparam.service.parametrageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class parametrageController {

    private parametrageService parametrageService;
    @GetMapping("/param/all")
    public ResponseEntity<List<parametrage>> getAllParametrages() {
        List<parametrage> allParametrages = parametrageService.getAllParametrages();
        if (!allParametrages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(allParametrages);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<parametrage> getParametrageById(@PathVariable Long id) {
        Optional<parametrage> parametrage = parametrageService.getParametrageById(id);
        return parametrage.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/param/add")
    public ResponseEntity<String> createParametrage(@RequestBody parametrage parametrage) {
        parametrage savedParametrage = parametrageService.saveParametrage(parametrage);
        if (savedParametrage != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Paramétrage ajouté avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du paramétrage.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParametrage(@PathVariable Long id) {
        if (parametrageService.deleteParametrage(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Paramétrage supprimé avec succès !");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le paramétrage avec l'ID spécifié n'existe pas.");
        }
    }
    @GetMapping("/params/type/{type}")
    public ResponseEntity<?> findByType( @PathVariable String type){
        List<parametrage> param = parametrageService.findByType(type);
        if(!param.isEmpty()){
            return new ResponseEntity(param , HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Aucune paramétre n'est trouvé avec ce type ", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/params/code/{code}")
    public ResponseEntity<?> findByCode( @PathVariable String code){
        Optional<parametrage> param = parametrageService.findByCode(code);
        if(param.isPresent()){
            return new ResponseEntity(param , HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Aucune paramétre n'est trouvé avec ce code ", HttpStatus.NOT_FOUND);
        }
    }
}
