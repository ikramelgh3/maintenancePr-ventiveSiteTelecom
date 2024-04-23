package net.elghz.actioncorrectiveservice.service;

import lombok.AllArgsConstructor;
import net.elghz.actioncorrectiveservice.entities.ActionCorrective;
import net.elghz.actioncorrectiveservice.enumeration.StatusGravite;
import net.elghz.actioncorrectiveservice.feign.AnomalieRestClient;
import net.elghz.actioncorrectiveservice.model.Anomalie;
import net.elghz.actioncorrectiveservice.model.Technicien;
import net.elghz.actioncorrectiveservice.repository.ActionCorrectiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class actionCorrectiveService {
    private ActionCorrectiveRepository repo;
    private AnomalieRestClient arepo;

    public ResponseEntity<?> effectuerActionCorrective(ActionCorrective actionCorrective, Long idAn){
        Anomalie a = arepo.getAnomalieById(idAn);
        a.setStatus(StatusGravite.RESOLUE);
        actionCorrective.setIdAnomalie(idAn);
        actionCorrective.setDateRealisation(new Date());
        actionCorrective.setAnomalie(a);
        repo.save(actionCorrective);
        return new ResponseEntity<>("Laction corrective est bien effectuer. ", HttpStatus.OK);

    }

    public ResponseEntity<?> getTechnicienAction(Long IdAc){
        ActionCorrective a = repo.findById(IdAc).get();
        Technicien technicien = arepo.getAnomalieById(a.getIdAnomalie()).getTechnicienResolvant();
        return new ResponseEntity<>(technicien, HttpStatus.OK);
    }

//    public ResponseEntity<?> getActionsCorrectivesOfAnomalie(Long idAn){
//
//    }


    public ActionCorrective getActionById(Long idAC){
         return  repo.findById(idAC).get();
    }

    public List<ActionCorrective> actionsCorrectivesAnomalies(Long IdAC){
        return repo.findByAnomalie_Id(IdAC);
    }

    public void deleteById(List<Long> idA){
        for(Long i : idA){
            Optional<ActionCorrective> optionalActionCorrective = repo.findById(i);
            if (optionalActionCorrective.isPresent()) {
                ActionCorrective a = optionalActionCorrective.get();
                repo.delete(a);
            } else {
                System.err.println("Action corrective avec l'ID " + i + " introuvable.");
            }
        }
    }

}
