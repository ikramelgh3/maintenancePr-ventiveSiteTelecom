package net.elghz.siteservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import net.elghz.siteservice.exception.SiteNotFoundException;
import net.elghz.siteservice.service.ActiviteService;
import net.elghz.siteservice.service.attributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController

public class activiteController {

   private ActiviteService ser;

    public activiteController(ActiviteService ser) {
        this.ser = ser;
    }

    @GetMapping("/activite/id/{id}")
    public ResponseEntity<?> getActiviteId(@PathVariable Long id){
        Optional<TypeActivite> a = ser.getActiviteId(id);
        if(a.isPresent()){
            return new ResponseEntity<>(a , HttpStatus.OK);
        }
        else {
            return  new ResponseEntity<>("Aucune activité n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/activite/all")
    public ResponseEntity<?> getActivities(){
        List<TypeActivite> activities = ser.allActivities();
        if(!activities.isEmpty()){
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("La liste des activités est vide", HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/activite/add")
    public ResponseEntity<String> addActivite(@RequestBody TypeActivite ac) {
        boolean added = ser.addTypeActivite(ac);
        if (added) {
            return new ResponseEntity<>("L'activté est bien ajouté", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("l'activite avec ce nom est déja existe", HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/activite/{id}")
    public ResponseEntity<String> updateActivite(@PathVariable Long id, @RequestBody TypeActivite updatedActv) {
        updatedActv.setId(id);
        boolean updated = ser.updateActivite(updatedActv);
        if (updated) {
            return new ResponseEntity<>("L'activite est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun activite n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/activite/{id}")
    public ResponseEntity<String> deleteActivite(@PathVariable Long id) {
        boolean deleted = ser.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>("L'activite est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun activite n'est trouvée avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/activites/site/{name}")
    public ResponseEntity<?> activitiesSite(@PathVariable String name){

        try {
            List <TypeActivite> s =ser.activitesSite(name);
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (SiteNotFoundException e) {
            return  new ResponseEntity<>(" Ce site n'a pas d'activités", HttpStatus.NOT_FOUND);
        }

    }

    //associe un site à une activite
    @PostMapping("/{typeActiviteId}/sites/{siteId}")
    public ResponseEntity<String> addSiteToTypeActivite(@PathVariable Long siteId, @PathVariable Long typeActiviteId) {
        ser.addSiteToTypeActivite(siteId, typeActiviteId);
        Optional<TypeActivite> a = ser.getActiviteId(typeActiviteId);
        String m = a.get().getName();
        return ResponseEntity.ok("Le site est bien ajouté à l'activité: "+m);
    }

    @DeleteMapping("/{typeActiviteId}/sites/{siteId}")
    public ResponseEntity<String> RemoveSiteFromTypeActivite(@PathVariable Long siteId, @PathVariable Long typeActiviteId) {
        ser.removeSiteFromTypeActivite(siteId, typeActiviteId);
        Optional<TypeActivite> a = ser.getActiviteId(typeActiviteId);
        String m = a.get().getName();
        return ResponseEntity.ok("Le site est bien supprimé de l'activité: "+m);
    }

    @GetMapping("/{typeActiviteId}/sites")
    public ResponseEntity<Set<Site>> getSitesByTypeActivite(@PathVariable Long typeActiviteId) {
        Set<Site> sites = ser.getSitesByTypeActivite(typeActiviteId);
        return ResponseEntity.ok().body(sites);
    }

    @PutMapping("/{siteId}/type-activites")
    public ResponseEntity<Void> updateSiteTypeActivite(@PathVariable Long siteId, @RequestBody List<Long> typeActiviteIds) {
        ser.updateSiteTypeActivite(siteId, typeActiviteIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sitesMu")
    public ResponseEntity<Set<Site>> getSitesByMultipleTypeActivites(@RequestBody List<Long> typeActiviteIds) {
        Set<Site> sites = ser.getSitesByMultipleTypeActivites(typeActiviteIds);
        return ResponseEntity.ok().body(sites);
    }

    @DeleteMapping("/{siteId}/type-activites")
    public ResponseEntity<Void> clearTypeActiviteForSite(@PathVariable Long siteId) {
        ser.clearTypeActiviteForSite(siteId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{typeActiviteId}/sites")
    public ResponseEntity<Void> clearSitesForTypeActivite(@PathVariable Long typeActiviteId) {
        ser.clearSitesForTypeActivite(typeActiviteId);
        return ResponseEntity.ok().build();
    }
}
