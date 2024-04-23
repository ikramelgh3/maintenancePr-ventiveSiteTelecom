package net.elghz.anomalieservice.service;

import lombok.AllArgsConstructor;
import net.elghz.anomalieservice.dto.AnomalieDTO;
import net.elghz.anomalieservice.dto.PhotoAnomalieDTO;
import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.entities.PhotoAnomalie;
import net.elghz.anomalieservice.mapper.mapper;
import net.elghz.anomalieservice.repository.PhotoAnomalieRepository;
import net.elghz.anomalieservice.repository.anomalieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class photoAnomalieService {
    private PhotoAnomalieRepository repo;
    private anomalieRepository anrepo;
    private mapper mp;

    public ResponseEntity<?> ajouterPhotosToAnomalie(Long idAn, List<PhotoAnomalieDTO>photos){

        Optional<Anomalie> anomalie = anrepo.findById(idAn);


        if (!anomalie.isPresent()) {
            return new ResponseEntity<>("L'anomalie n'existe pas.", HttpStatus.NOT_FOUND);
        }

        for(PhotoAnomalieDTO p: photos){
            PhotoAnomalie ph = mp.from(p);
            anomalie.get().addPhoto(ph);

        }
        anrepo.save(anomalie.get());
        return new ResponseEntity<>("Les photos sont bien joindre à l'anomalie", HttpStatus.OK);
    }




}
