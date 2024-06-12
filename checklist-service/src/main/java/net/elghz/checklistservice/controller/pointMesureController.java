package net.elghz.checklistservice.controller;

import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import net.elghz.checklistservice.model.typeEquipement;
import net.elghz.checklistservice.repository.pointMesureRepo;
import net.elghz.checklistservice.services.checklistService;
import net.elghz.checklistservice.services.importerPointsMesures;
import net.elghz.checklistservice.services.pointMesureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/pointMesure")
public class pointMesureController {

    @Autowired
    pointMesureService ser;
    @Autowired
    importerPointsMesures importer;@Autowired
    pointMesureRepo repo;


    @GetMapping("/rougrouperChklist")
    public void regrouper(@RequestBody PointMesure p ,@PathVariable Long id){
          ser.regrouperPointsMesureParTypeEtAjouterChecklist();
    }
    @GetMapping("/checkByTypeEqui/{id}")
    public ChecklistDTO checklistByTypeEqui(@PathVariable Long id){
        return ser.getCheBypeEq(id);
    }

    @GetMapping("/checklistByTypeEqui/{id}")
    public List<ChecklistDTO> checklistByType(@PathVariable Long id){
        return ser.getCheByTypeEq(id);
    }
    @PostMapping ("/ptM/add")
    public ResponseEntity<?> addPT(@RequestBody PointMesureDTO ch){
        return  ser.addPT(ch);
    }

    @PostMapping ("/ptMs/add")
    public ResponseEntity<?> addPTS(@RequestBody List<PointMesureDTO> ch){
        return  ser.addPTS(ch);
    }
    @DeleteMapping("/ptM/delete/{id}")
    public void deleteCk(@PathVariable Long id){
           ser.deletePT(id);
    }


    @DeleteMapping("/ptM")
    public void deleteChecklist(@RequestBody PointMesure checklistch){
        repo.delete(checklistch);
    }
    @PostMapping("/import-checklists")
    public List<PointMesureDTO> importPointsMesures(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }


        try {
            return   importer.importPointsDeMesureFromExcel( file.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();

        } catch (DataIntegrityViolationException ex) {
            return new ArrayList<>();

        }
    }

    @GetMapping("/checklists/equip/{id}")
    List<PointMesureDTO> getChecklistsByEqui( @PathVariable Long id){
        return ser.getChecklistByEquip(id);
    }

    @GetMapping("/checklist/all")
    public List<PointMesureDTO> findAll(){
         return ser.allChecklist();
    }
    @GetMapping("/checklist/id/{id}")
    public PointMesureDTO findById(@PathVariable Long id){
        return  ser.findById(id);
    }



    @GetMapping("/checklist/exist")
    public boolean existeByName(@RequestParam String name, @RequestParam Long id) {
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);
        return repo.existsByAttributAndIdIsNot(decodedName, id);
    }

    @PutMapping("/update/pointMesure/{id}")
    public PointMesureDTO updatePointeMEsure(@PathVariable Long id, @RequestBody PointMesure p){
         return  ser.updatePointMesure(id, p);
    }
    @PostMapping("/add/checklist/{idType}")
    public void addPointMesureWithGroup(@RequestBody PointMesure p, @PathVariable Long idType){
           ser.addPointMesureGroup(p, idType);
    }

    @GetMapping ("/check/ptMesure/{name}")
    public  Boolean checkIfPointMesureExistByName(@PathVariable String name){
        return  repo.existsByAttribut(name);
    }
}

