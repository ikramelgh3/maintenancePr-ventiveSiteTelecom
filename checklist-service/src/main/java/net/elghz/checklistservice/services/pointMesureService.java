package net.elghz.checklistservice.services;

import feign.FeignException;
import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import net.elghz.checklistservice.feign.EquipementRestClient;
import net.elghz.checklistservice.feign.RespoRestClient;
import net.elghz.checklistservice.mapper.mapper;
import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.typeEquipement;
import net.elghz.checklistservice.repository.checklistRepo;
import net.elghz.checklistservice.repository.pointMesureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class pointMesureService {

    @Autowired private pointMesureRepo repo;

    @Autowired private checklistRepo crepo;
    @Autowired private mapper mp;
    @Autowired private EquipementRestClient equipRest;
    @Autowired private RespoRestClient respoRest;

    public ResponseEntity<?> addPT(PointMesureDTO ptDto){
        PointMesure p = mp.from(ptDto);
        repo.save(p);
        return new ResponseEntity<>("Point de mesure bien ajouté" , HttpStatus.OK);
    }
    public ResponseEntity<?> addPTS(List <PointMesureDTO> ptDto){
        List<PointMesure> pointMesures = new ArrayList<>();
         for ( PointMesureDTO p :ptDto){
             PointMesure pp = mp.from(p);
            pointMesures.add(pp);
         }

        repo.saveAll(pointMesures);
        return new ResponseEntity<>("Les points de mesure sont bien ajoutés" , HttpStatus.OK);
    }
    public PointMesureDTO findById(Long id) {
        Optional<PointMesure> ch = repo.findById(id);

        if (ch.isPresent()) {
            PointMesureDTO dto = mp.from(ch.get());

//            // Initialize these fields to null
//            dto.setRespoMaint(null);
//            dto.setRespo_Id(null);

//            // Fetch and set ResponsableMaintenance only if respo_Id is not null
//            if (dto.getRespo_Id() != null) {
//                try {
//                    ResponsableMaintenance rsp = respoRest.findById(dto.getRespo_Id());
//                    dto.setRespoMaint(rsp);
//                } catch (FeignException.NotFound e) {
//                    // Log the error if needed and keep respoMaint as null
//                    System.out.println("ResponsableMaintenance not found for id: " + dto.getRespo_Id());
//                }
//            }
            // Fetch and set TypeEquipement
            typeEquipement e = equipRest.findTypeEquipemntById(dto.getTypeEquipementId());
            dto.setTypeEquipent(e);

            return dto;
        } else {
            return null;
        }
    }


    public List<PointMesureDTO> allChecklist() {
        List<PointMesure> checklists = repo.findAll();
        System.out.println("gettttt");
        List<PointMesureDTO> dto = checklists.stream().map(mp::from).collect(Collectors.toList());

        for (PointMesureDTO c : dto) {
//            Long idr = c.getRespo_Id();
//            if (idr != null) {
//                ResponsableMaintenance r = respoRest.findById(idr);
//                if (r != null) {
//                    c.setRespoMaint(r);
//                }
//            } else {
//                c.setRespoMaint(null);
//            }

            Long idt = c.getTypeEquipementId();
            if (idt != null) {
                try {
                    typeEquipement r = equipRest.findTypeEquipemntById(idt);
                    if (r != null) {
                        c.setTypeEquipent(r);
                    } else {
                        c.setTypeEquipent(null); // Définir le type d'équipement sur null si non trouvé
                    }
                } catch (FeignException.NotFound ex) {
                    // Gérer le cas où le type d'équipement n'existe pas
                    // Par exemple, définir le type d'équipement sur null
                    c.setTypeEquipent(null);
                }
            } else {
                c.setTypeEquipent(null);
            }

            repo.save(mp.from(c));
        }

        for (PointMesureDTO p : dto) {
            System.out.println(p.getAttribut());
        }

        System.out.println("size" + dto.size());

        if (dto.size() == 0) {
            return null;
        } else {
            return dto;
        }
    }

    public ResponseEntity <?> deletePT( Long id)
    {
        Optional<PointMesure> ch = repo.findById(id);

        if(ch.isPresent()){
           repo.delete(ch.get());
            return new ResponseEntity<>("Point de mesure supprimée", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune point de mesure n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }
/*
    public ResponseEntity<?> updatePT(Long id){
        Optional<PointMesure> ch = repo.findById(id);

        if(ch.isPresent()){
            PointMesureDTO dto = mp.from(ch.get());
            mp.updatePointMesure(dto, ch.get());
            return new ResponseEntity<>("Point de mesure est bien modifie", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Aucune point de mesure n'est trouvée avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
    }
*/
    public ResponseEntity<?> allPTS(){

       List<PointMesure> checklists= repo.findAll();
        List<PointMesureDTO> dto =  checklists.stream().map(mp::from).collect(Collectors.toList());
       if(dto.size()!=0){
            return new ResponseEntity<>(dto, HttpStatus.OK);}
       else{
           return new ResponseEntity<>("Aucune point de mesure n'est trouvé", HttpStatus.NOT_FOUND);
       }
    }


    public List<PointMesureDTO> getChecklistByEquip(Long idEq){


        List<PointMesure> ch = repo.findByTypeEquipementId(idEq);
        typeEquipement e =equipRest.findTypeEquipemntById(idEq);
        for(PointMesure c :ch ){
//            Long IdR = c.getRespo_Id();
//            if(IdR==null){
//                c.setRespoMaint(null);
//            }
//            ResponsableMaintenance r = respoRest.findById(IdR);
//            c.setRespoMaint(r);
            c.setTypeEquipent(e);
        }

        return ch.stream().map(mp::from).collect(Collectors.toList());

    }


//    public PointMesureDTO updatePointMesure(Long id, PointMesure checklist){
//         PointMesure c = repo.findById(id).get();
//         c.setAttribut(checklist.getAttribut());
//         c.setTypeEquipent(checklist.getTypeEquipent());
//         c.setTypeEquipementId(checklist.getTypeEquipementId());
//         c.setResultatsPossibles(checklist.getResultatsPossibles());
//         c.setRespoMaint(checklist.getRespoMaint());
//         c.setRespo_Id(checklist.getRespo_Id());
//
//         repo.save(c);
//         return mp.from(c);
//
//
//    }

    public PointMesureDTO updatePointMesure(Long id, PointMesure updatedPointMesure) {
        PointMesure existingPointMesure = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PointMesure not found with id " + id));

        existingPointMesure.setAttribut(updatedPointMesure.getAttribut());

        if (updatedPointMesure.getResultatsPossibles() != null) {
            existingPointMesure.getResultatsPossibles().clear();
            existingPointMesure.getResultatsPossibles().addAll(updatedPointMesure.getResultatsPossibles());
        }
         repo.save(existingPointMesure);
         return mp.from(existingPointMesure);

    }


    public PointMesureDTO addPointMesureGroup(PointMesure  p , Long idT){

        typeEquipement t = equipRest.findTypeEquipemntById(idT);
        p.setTypeEquipementId(t.getId());
        p.setTypeEquipent(t);
        repo.save(p);
        ChecklistDTO d = getCheBypeEq(idT);
        if(d==null){
            Checklist checklist = new Checklist();
            checklist.getMeasurementPoints().add(p);
            checklist.setTypeEquipementId(idT);
            crepo.save(checklist);
        }
        else{
            Checklist chesfind = mp.from(d);
            chesfind.getMeasurementPoints().add(p);
            chesfind.setTypeEquipementId(idT);
            crepo.save(chesfind);
        }

        return  mp.from(p);

    }

    public ChecklistDTO addPointToChecklistByType(PointMesureDTO p,Long idT){
         ChecklistDTO c =getCheBypeEq(idT);
         c.getMeasurementPoints().add(p);
         return  c;
    }

    public  ChecklistDTO getCheBypeEq(Long id){
       ChecklistDTO ch ;
        List<Checklist> checklists =crepo.findAll();
        for(Checklist c :checklists){

            if( c.getTypeEquipementId()==id){

               return  mp.from(c);
            }
        }
        return  null;
    }




    public  List<ChecklistDTO> getCheByTypeEq(Long id){
        List<ChecklistDTO> checklistDTOS = new ArrayList<>();
         List<Checklist> checklists =crepo.findAll();
         for(Checklist c :checklists){

             if( c.getTypeEquipementId()==id){
                  checklistDTOS.add(mp.from(c));
             }
         }
         return  checklistDTOS;
    }

    public void regrouperPointsMesureParTypeEtAjouterChecklist() {
        // Récupérer tous les points de mesure de la base de données
        List<PointMesure> pointsMesure = repo.findAll();

        // Regrouper les points de mesure par type d'équipement
        Map<typeEquipement, List<PointMesure>> pointsMesureParType = new HashMap<>();
        for (PointMesure pointMesure : pointsMesure) {

            Long dt=pointMesure.getTypeEquipementId();
            typeEquipement typeEquipement = equipRest.findTypeEquipemntById(dt);
            pointsMesureParType.computeIfAbsent(typeEquipement, k -> new ArrayList<>()).add(pointMesure);
        }

        // Associer chaque liste de points de mesure à une checklist correspondante
        for (Map.Entry<typeEquipement, List<PointMesure>> entry : pointsMesureParType.entrySet()) {
            typeEquipement typeEquipement = entry.getKey();
            List<PointMesure> pointsMesurePourType = entry.getValue();

            // Créer une nouvelle checklist pour ce type d'équipement
            Checklist checklist = new Checklist();
            checklist.setTypeEquipent(typeEquipement);

            // Associer les points de mesure à cette checklist
            Set<PointMesure> measurementPoints = new HashSet<>(pointsMesurePourType);
            checklist.setMeasurementPoints(measurementPoints);

            // Enregistrer la checklist en base de données
            crepo.save(checklist);
        }
    }




}
