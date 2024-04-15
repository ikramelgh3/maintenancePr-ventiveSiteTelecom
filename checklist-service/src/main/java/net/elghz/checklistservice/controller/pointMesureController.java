package net.elghz.checklistservice.controller;

import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.services.checklistService;
import net.elghz.checklistservice.services.pointMesureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class pointMesureController {

    @Autowired
    pointMesureService ser;
    @GetMapping("/ptM/all")
    public ResponseEntity<?> findAll(){
         return ser.allPTS();
    }
    @PostMapping ("/ptM/add")
    public ResponseEntity<?> addPT(@RequestBody PointMesureDTO ch){
        return  ser.addPT(ch);
    }

    @PostMapping ("/ptMs/add")
    public ResponseEntity<?> addPTS(@RequestBody List<PointMesureDTO> ch){
        return  ser.addPTS(ch);
    }
    @GetMapping("/ptM/id/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
         return  ser.findById(id);
    }
    @DeleteMapping("/ptM/delete/{id}")
    public ResponseEntity<?> deleteCk(@PathVariable Long id){
         return  ser.deletePT(id);
    }


}
