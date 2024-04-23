package net.elghz.anomalieservice.controller;

import lombok.AllArgsConstructor;
import net.elghz.anomalieservice.dto.AnomalieDTO;
import net.elghz.anomalieservice.dto.PhotoAnomalieDTO;
import net.elghz.anomalieservice.entities.PhotoAnomalie;
import net.elghz.anomalieservice.service.photoAnomalieService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
public class photoAnomalieController {

    private photoAnomalieService service;

    @PostMapping("/joindrePhoto/Anomalie")
    public ResponseEntity<?> joindrePhotoAnomalie(@RequestParam Long id, @RequestBody List<PhotoAnomalieDTO>photos){
        return service.ajouterPhotosToAnomalie(id,photos);
    }



}
