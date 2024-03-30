package net.elghz.siteservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.exception.ActiviteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.elghz.siteservice.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController

public class siteController {


    private siteService serv;

    public siteController(siteService serv) {
        this.serv = serv;
    }

    @GetMapping("/sites")
    public ResponseEntity<?> allSites() {
        List<Site> s =serv.allSites();
        if (!s.isEmpty()) {
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
        else {
           return  new ResponseEntity<>("Aucune site n'est trouvé", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getSitesByType(@PathVariable String type) {
        try {
            SiteType siteType = SiteType.valueOfIgnoreCase(type.toUpperCase());
            Optional<List<Site>> sites = serv.findByType(siteType);
            if (!sites.isEmpty()) {
                return new ResponseEntity<>(sites, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Aucun site trouvé pour le type: " + type, HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>("Aucun site trouvé pour le type: " + type, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/site/id/{id}")
    public ResponseEntity<?> findSiteById(@PathVariable Long id){
       Optional<Site> s = serv.findById(id);
       if (s.isPresent()){
           return ResponseEntity.ok(s.get());
       }
       else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune site n'est trouvé avec ce ID" );
       }

    }

    @DeleteMapping("site/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = serv.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>("Le site est supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucune site n'est trouvé avec ce id" +id , HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/site/add")
    public ResponseEntity<?> saveSite(@RequestBody Site site) {
        return serv.saveSite(site);
    }



    @PutMapping("/site/{id}")
    public ResponseEntity<String> updateSite(@PathVariable Long id, @RequestBody Site updatedSite) {
        updatedSite.setId(id);
        boolean updated  = serv.updateSite(updatedSite);
        if (updated) {
            return new ResponseEntity<>("Le site est bien modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Aucun site n'est trouvé avec ce Id: " +id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{sideId}/activities")
    public ResponseEntity<Set<TypeActivite>> getSitesByTypeActivite(@PathVariable ("sideId") Long s) {
        Set<TypeActivite> sites = serv.getActivitiesBySite(s);
        return ResponseEntity.ok().body(sites);
    }
}

