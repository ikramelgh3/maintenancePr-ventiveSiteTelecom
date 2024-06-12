package net.elghz.siteservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.PhotoDTO;
import net.elghz.siteservice.entities.Photo;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.repository.SiteRepository;
import net.elghz.siteservice.service.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sites")
@AllArgsConstructor
public class photoController {

    private ImageService ser;
    private SiteRepository repo;


    @PostMapping("image/ajouterImage/{p}")
    public boolean ajouterImage(@RequestParam("imageFile") MultipartFile file, @PathVariable Long p) {
        return ser.ajouterImage(file,p);
    }

    /*@GetMapping("image/imageByLibelle")
    public Image imageByLibelle(@RequestParam(name="libelle") String libelle) {
        try {
            return ser.getImage(libelle);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }
    */
    @GetMapping("image/toutesLesImages")
    public List<PhotoDTO> toutesLesImages() {
        try {
            return ser.toutesLesImages();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("image/supprimerImage")
    public Boolean supprimerImage(@RequestParam(name="id") Long id) {
        try {
            ser.supprimerById(id);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping("image/imageById/{id}")
    public PhotoDTO imageById(@PathVariable Long id) {
        try {
            return ser.imageById(id);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }
    @GetMapping("image/imagesProduit")
    public List<Photo> imagesProduit(@RequestParam(name="id") Long id) {
        try {
            Site p = repo.findById(id).get();
            return ser.imageByProduit(p);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }
    @PutMapping("image/modifierImage/ids/idI")
    public Photo modifierImage(@RequestParam("imageFile") MultipartFile file,@PathVariable Long ids,@PathVariable Long idI) {
        try {
            return ser.modifierImage(file,ids,idI);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
    }
}
