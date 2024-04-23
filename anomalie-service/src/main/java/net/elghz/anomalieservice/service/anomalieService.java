package net.elghz.anomalieservice.service;
import lombok.AllArgsConstructor;
import net.elghz.anomalieservice.dto.AnomalieDTO;
import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.enumeration.MomentPrisephoto;
import net.elghz.anomalieservice.enumeration.StatusGravite;
import net.elghz.anomalieservice.feign.ActionCorrectiveRestClient;
import net.elghz.anomalieservice.feign.InterventionRestClient;
import net.elghz.anomalieservice.feign.technicienRestClient;
import net.elghz.anomalieservice.mapper.mapper;
import net.elghz.anomalieservice.model.ActionCorrective;
import net.elghz.anomalieservice.model.Interevntion;
import net.elghz.anomalieservice.model.Technicien;
import net.elghz.anomalieservice.repository.anomalieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class anomalieService {

    private anomalieRepository rep;
    private technicienRestClient trepo;
    private InterventionRestClient irepo;
    private mapper mp;
    private ActionCorrectiveRestClient arepo;

    public ResponseEntity<?> declarerAnomalie(AnomalieDTO anomalieDTO) {

        Anomalie an = mp.from(anomalieDTO);
        Optional<Anomalie> anomalie = rep.findById(an.getId());

        if (anomalie.isPresent()) {
            return new ResponseEntity<>("L'anomalie existe déjà.", HttpStatus.OK);
        }
        Technicien technicien = trepo.findTechnicienById(an.getId_TechnicienD());
        Interevntion interevntion = irepo.findInterventionById(an.getId_Intervention());
        if (interevntion == null) {
            an.setInterevntion(null);
            return new ResponseEntity<>("Aucune interevntion n'est trouvée avec cet ID.", HttpStatus.NOT_FOUND);
        }


        if (technicien == null) {
            an.setTechnicienDetecteur(null);
            return new ResponseEntity<>("Aucun technicien n'est trouvé avec cet ID.", HttpStatus.NOT_FOUND);
        }

        an.setTechnicienDetecteur(technicien);
        an.setInterevntion(interevntion);
        an.setDateDetection(new Date());
        an.setDateResolution(null);
        an.setStatus(StatusGravite.OUVERTE);

        rep.save(an);

        return new ResponseEntity<>("L'anomalie a bien été ajoutée.", HttpStatus.OK);
    }

    public List<AnomalieDTO> getAnomaliesOuvertes() {

        List<Anomalie> anomalieList = rep.findByStatus(StatusGravite.OUVERTE);
        for (Anomalie a : anomalieList) {
            Long id = a.getId_TechnicienD();
            Technicien tech = trepo.findTechnicienById(id);
            a.setTechnicienDetecteur(tech);
            if (a.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(a.getId_TechnicienR());
                a.setTechnicienResolvant(technicienR);
            } else {
                a.setTechnicienResolvant(null);
            }
            Interevntion i = irepo.findInterventionById(a.getId_Intervention());
            a.setInterevntion(i);
            a.setId_Intervention(a.getId_Intervention());
        }
        return anomalieList.stream().map(mp::from).collect(Collectors.toList());
    }

    public List<AnomalieDTO> getAnomaliesEnRetard() {

        List<Anomalie> anomalieList = rep.findByStatus(StatusGravite.EN_RETARD);
        for (Anomalie a : anomalieList) {
            Long id = a.getId_TechnicienD();
            Technicien tech = trepo.findTechnicienById(id);
            a.setTechnicienDetecteur(tech);
            if (a.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(a.getId_TechnicienR());
                a.setTechnicienResolvant(technicienR);
            } else {
                a.setTechnicienResolvant(null);
            }
            Interevntion i = irepo.findInterventionById(a.getId_Intervention());
            a.setInterevntion(i);
        }
        return anomalieList.stream().map(mp::from).collect(Collectors.toList());
    }

    public void metterAjourStatus(Long idAnomalie) {
        Anomalie anomalie = rep.findById(idAnomalie).orElse(null);
        if (anomalie != null) {
            if (estEnRetard(anomalie.getDateDetection())) {
                anomalie.setStatus(StatusGravite.EN_RETARD);
                rep.save(anomalie);
            }
        }
    }


    public boolean updateAnomalieStatus(Long id, StatusGravite newStatus) {
        Optional<Anomalie> optionalAnomalie = rep.findById(id);
        if (optionalAnomalie.isPresent()) {
            Anomalie anomalie = optionalAnomalie.get();
            anomalie.setStatus(newStatus);
            rep.save(anomalie);
            return true;
        } else {
            return false;
        }
    }

    public boolean estEnRetard(Date dateDetection) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateDetection);
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Date dateLimite = cal.getTime();
        Date dateActuelle = new Date();
        return dateActuelle.after(dateLimite);
    }

    // envoyer alert ou email ou les taches qui sont en retard
    public AnomalieDTO getAnomalie(Long id) {
        Optional<Anomalie> optionalAnomalie = rep.findById(id);
        if (optionalAnomalie.isPresent()) {
            Anomalie an = optionalAnomalie.get();
            Technicien technicienD = trepo.findTechnicienById(an.getId_TechnicienD());
            Interevntion interevntion = irepo.findInterventionById(an.getId_Intervention());

            if (interevntion == null) {
                an.setInterevntion(null);
            }

            if (technicienD == null) {
                an.setTechnicienDetecteur(null);
            }
            if (an.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(an.getId_TechnicienR());
                an.setTechnicienResolvant(technicienR);

                an.setActionsCorrectives(arepo.actionsCorrectivesOfAnomalie(an.getId()));

            } else {
                an.setTechnicienResolvant(null);
            }

            an.setTechnicienDetecteur(technicienD);
            an.setInterevntion(interevntion);
            return mp.from(an);
        }
        return null;
    }

    public boolean deleteAnomalie(Long id) {
        Optional<Anomalie> optionalAnomalie = rep.findById(id);
        if (optionalAnomalie.isPresent()) {
            Anomalie anomalie = optionalAnomalie.get();
            if (anomalie.getStatus() == StatusGravite.FERMEE) {
                List<ActionCorrective> actionCorrectives = arepo.actionsCorrectivesOfAnomalie(anomalie.getId());
                List<Long> ids = new ArrayList<>();
                for(ActionCorrective a :actionCorrectives){
                    ids.add(a.getId());
                    a.setAnomalie(null);
                    a.setIdAnomalie(null);
                }
                arepo.deleteActionsCorrective(ids);
                rep.delete(anomalie);
                //return "Anomalie supprimée avec succès.";
                return true;
            } else {
                //return "Vous ne pouvez pas supprimer cette anomalie tant qu'elle n'est pas fermée.";
                return false;
            }
        } else {
            //return "Anomalie introuvable.";
            return false;
        }
    }


    public List<AnomalieDTO> getAnomalieDetecteByTech(Long idTech) {
        Technicien tech = trepo.findTechnicienById(idTech);
        if (tech == null) {
            return null;
        }

        List<Anomalie> anomalieList = rep.getAnomaliesByIdTech(idTech);
        for (Anomalie a : anomalieList) {
            a.setId_TechnicienD(idTech);
            a.setTechnicienDetecteur(trepo.findTechnicienById(idTech));
            Interevntion i = irepo.findInterventionById(a.getId_Intervention());
            a.setInterevntion(i);
            if (a.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(a.getId_TechnicienR());
                a.setTechnicienResolvant(technicienR);
                a.setActionsCorrectives(arepo.actionsCorrectivesOfAnomalie(a.getId()));
            } else {
                a.setTechnicienResolvant(null);
            }

        }
        return anomalieList.stream().map(mp::from).collect(Collectors.toList());
    }

    public List<AnomalieDTO> anomaliesDetectedInInterevntion(Long idIntervention) {
        List<Anomalie> aanomalies = rep.getAnomalieDetectedInIntervention(idIntervention);
        for (Anomalie a : aanomalies) {
            a.setInterevntion(irepo.findInterventionById(a.getId_Intervention()));
            a.setTechnicienDetecteur(trepo.findTechnicienById(a.getId_TechnicienD()));
            if (a.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(a.getId_TechnicienR());
                a.setTechnicienResolvant(technicienR);
                a.setActionsCorrectives(arepo.actionsCorrectivesOfAnomalie(a.getId()));
            } else {
                a.setTechnicienResolvant(null);
            }
        }
        return aanomalies.stream().map(mp::from).collect(Collectors.toList());
    }

    public List<AnomalieDTO> getAllAnomalies() {

        List<Anomalie> anomalieList = rep.findAll();
        for (Anomalie a : anomalieList) {
            Long idTech = a.getId_TechnicienD();
            Long idIn = a.getId_Intervention();
            Technicien tech = trepo.findTechnicienById(idTech);
            Interevntion inter = irepo.findInterventionById(idIn);
            a.setTechnicienDetecteur(tech);
            if (a.getId_TechnicienR() != null) {
                Technicien technicienR = trepo.findTechnicienById(a.getId_TechnicienR());
                a.setTechnicienResolvant(technicienR);
                a.setActionsCorrectives(arepo.actionsCorrectivesOfAnomalie(a.getId()));
            } else {
                a.setTechnicienResolvant(null);
            }
            a.setInterevntion(inter);
        }

        return anomalieList.stream().map(mp::from).collect(Collectors.toList());
    }


    public String getStatusAnomalie(Long id) {
        Anomalie A = rep.findById(id).get();
        if (A.getStatus().equals(StatusGravite.OUVERTE)) {
            return "OUVERTE";
        } else
            return "ERREUR";
    }


    public ResponseEntity<?> attribuerAnomalieToTechnicien(Long idAn, Long idT) {
        Optional<Anomalie> optionalAnomalie = rep.findById(idAn);
        if (optionalAnomalie.isPresent()) {
            Anomalie an = optionalAnomalie.get();
            Technicien tech = trepo.findTechnicienById(idT);
            an.setTechnicienResolvant(tech);
            an.setId_TechnicienR(idT);
            rep.save(an);
            return new ResponseEntity<>("L'anomalie est bien attribuée au technicien: " + tech.getNom(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Aucune anomalie n'est trouvé avec ce id: " + idAn, HttpStatus.NOT_FOUND);


    }

    public List<ActionCorrective> actionsCorrectiveForAnomalie(Long idA){
        return rep.findById(idA).get().getActionsCorrectives();

    }



}
