package net.elghz.interventionservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.PrioriteEnum;
import net.elghz.interventionservice.enumeration.TypeIntervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import net.elghz.interventionservice.feign.PlannigRestClient;
import net.elghz.interventionservice.feign.TechnicienRestClient;
import net.elghz.interventionservice.feign.checklistFeignClinet;
import net.elghz.interventionservice.feign.equipementRestClient;
import net.elghz.interventionservice.mapper.mapper;
import net.elghz.interventionservice.model.*;
import net.elghz.interventionservice.repository.interventionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class interventionService {

    private interventionRepo repo;
    private mapper mp;
    private PlannigRestClient prepo;
    private TechnicienRestClient erepo;
    private equipementRestClient eqRepo;
    private checklistFeignClinet chCli;
    @Autowired
    EmailService emailService;




//
//    public List<TechnicienDTO> technicensDisponibleDandLadateIntervention(Date d){
//
//    }
public List<TechnicienDTO> getAvailableTechniciansForDay( LocalDate date) {
    List<TechnicienDTO> techniciens = erepo.getTechnicians();
    // Filtrer les techniciens pour ne récupérer que ceux qui sont disponibles pour la journée donnée
    return techniciens.stream()
            .filter(technicien -> isTechnicianAvailableForDay(technicien, date))
            .collect(Collectors.toList());
}

    // Méthode pour vérifier si un technicien est disponible pour une journée donnée
    private boolean isTechnicianAvailableForDay(TechnicienDTO technicien, LocalDate date) {
        // Récupérer les interventions prévues pour le technicien pour la journée donnée
        List<InterventionDTO> interventions = getInterventionsOfTehnicien1(technicien.getId()).stream()
                .filter(intervention -> {
                    LocalDate interventionDate = intervention.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return interventionDate.equals(date);
                })
                .collect(Collectors.toList());

        // Le technicien est disponible si aucune intervention n'est prévue pour cette journée
        return interventions.isEmpty();
    }
    public List<TechnicienDTO> technicienExisteVilleCommeEquipement(Long idI){
        Intervention i = repo.findById( idI).get();
        Equipement e = eqRepo.getEquiById(i.getId_Equipement());
       String villeEqui= eqRepo.getVilleEqui(e.getId());
       return  erepo.filterTechnicienByCity(villeEqui);
    }

    public  InterventionDTO addInterventionComplet(InterventionDTO dt, String idTech, Long idPl, Long idEqui ,String idRes){
        Planning p = prepo.findPlanningById(idPl);
        TechnicienDTO tech = erepo.getUser(idTech);
        TechnicienDTO respo = erepo.getUser(idRes);
        Equipement eq= eqRepo.getEquiById(idEqui);
        Intervention i = mp.from(dt);
        i.setTechnicien(tech);
        i.setResponsable(respo);
        i.setId_Respo(idRes);
        i.setId_Techn(idTech);
        i.setPlanning(p);
        i.setId_Planning(idPl);
        i.setEquipement(eq);
        i.setId_Equipement(idEqui);
        i.setStatus(statusIntervention.PLANIFIEE);
        repo.save(i);

        return  mp.from(i);

    }

    public InterventionDTO addInterventionCompletCorrective(InterventionDTO dt, String idTech, Long idEqui ,String idRes){

        TechnicienDTO tech = erepo.getUser(idTech);
        TechnicienDTO respo = erepo.getUser(idRes);
        Equipement eq= eqRepo.getEquiById(idEqui);
        Intervention i = mp.from(dt);
        i.setTechnicien(tech);
        i.setResponsable(respo);
        i.setId_Respo(idRes);
        i.setId_Techn(idTech);

        // Assurez-vous de créer un objet Planning par défaut ou de fournir une valeur valide

        i.setPlanning(null); // Définissez le planning pour l'intervention corrective

        i.setEquipement(eq);
        i.setId_Equipement(idEqui);
        i.setStatus(statusIntervention.PLANIFIEE);
        repo.save(i);

        return  mp.from(i);
    }

    public  checklist poinMesureByTypeEqui(Long id){

        checklist n = new checklist();
      for (PointMesureDTO p : chCli.getChecklistsByEqui(id)){
           n.getMeasurementPoints().add(p);
      }
      Long idI = 5L;
      n.setId(idI);
      chCli.addCHecklist(n);
        return n;

    }
    public checklist getChecklistOfEqui(Long id) {
        // Récupérer l'équipement par son identifiant
        Equipement e = eqRepo.getEquiById(id);

        // Récupérer le type d'équipement de cet équipement
        typeEquipementDTO tt = e.getTypeEquipementt();

        // Récupérer les points de mesure associés à ce type d'équipement
        List<PointMesureDTO> po = chCli.getChecklistsByEqui(tt.getId());

        // Créer une nouvelle checklist
        checklist check = new checklist();

        // Ajouter les points de mesure récupérés à la checklist
        List<PointMesureDTO> pp = (List<PointMesureDTO>) check.getMeasurementPoints();
        for (PointMesureDTO pt : po) {
            pp.add(pt);
        }

        // Retourner la checklist remplie
        return check;
    }

    public void  sendEmailToTechnicien(String  techn, Long idI  ,String idRespo){
        String subject = "Planification d'intervention";

        TechnicienDTO tech = erepo.getUser(techn);
        Intervention i = repo.findById( idI).get();
Equipement eq = eqRepo.getEquiById(i.getId_Equipement());
        TechnicienDTO respo = erepo.getUser(idRespo);
         i.setTechnicien(tech);
         i.setResponsable(respo);
        String text = String.format(
                "Bonjour %s %s,\n\n" +
                        "Nous vous informons par la présente de la planification d'une intervention pour vous. Voici les détails de l'intervention :\n\n" +
                        "- Titre d'intervention : %s\n" +
                        "- Equipement concerné : %s+ Numéro de série: %s\n" +
                        "- Date de début : %s\n" +
                        "- Heure de début : %s\n" +
                        "- Priorité : %s\n" +
                        "- Adresse : %s\n" +
                        "Veuillez prendre note que vous êtes responsable de l'intervention dans son intégralité. Assurez-vous d'arriver à l'heure.\n\n" +
                        "Pour plus de détails concernant cette intervention, veuillez consulter votre compte sur TelecomTrack.\n\n" +
                        "Si des questions ou des préoccupations surviennent concernant cette intervention, n'hésitez pas à me contacter.\n\n" +
                        "Merci pour votre collaboration et votre professionnalisme.\n\n" +
                        "Cordialement,\n\n" +
                        "[%s %s]\n" +
                        "Responsable de maintenance\n" +
                        "[TelecomTrack]",
                tech.getFirstName(), tech.getLastName(),i.getName(),eq.getNom(),eq.getNumeroSerie(), i.getDateDebut(), i.getHeureDebut(), i.getPriorite(),eqRepo.getLocalisationOfEquip(i.getId_Equipement())  , i.getResponsable().getFirstName(),i.getResponsable().getLastName()
        );


        emailService.sendEmail(tech.getEmail(), subject, text);
    }

    public boolean checkPlanningExists(String name) {
        return repo.existsByName(name);
    }
    public ResponseEntity<?> addIntervention(InterventionDTO pl){
        Intervention pln = mp.from(pl);
        Optional<Intervention> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){
            repo.save(pln);
            return new ResponseEntity<>("L'intervention est bien planifiée." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja une intervention avec ce nom." , HttpStatus.OK);
    }

    public Boolean ajouterInterventionToPlanning(Long IdPlanning, InterventionDTO pl){
        Intervention pln = mp.from(pl);
        Optional<Intervention> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){

            pln.setId_Planning(IdPlanning);
            pln.setPlanning(prepo.findPlanningById(IdPlanning));
            repo.save(pln);
            return true;

        }
        else {
           return false;
        }



    }

    public ResponseEntity<?> findInterv( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());
        String idEqui = dto.getId_Techn();
        TechnicienDTO e = erepo.getUser(idEqui);
        dto.setTechnicien(e);
        Equipement equipement = eqRepo.getEquiById(dto.getId_Equipement());
        dto.setEquipement(equipement);
        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }


    public InterventionDTO findIntervention( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return null;
        }

        InterventionDTO  dto = mp.from(planningMaintenance.get());
        String idEqui = dto.getId_Techn();

        if(idEqui==null){
            return  dto;
        }
        TechnicienDTO e = erepo.getUser(idEqui);
        if(e==null){
            return  dto;
        }
        Equipement eq = eqRepo.getEquiById(dto.getId_Equipement());
        if(e==null){
            return  dto;
        }
        dto.setEquipement(eq);
        dto.setTechnicien(e);
        return  dto;
    }

    public ResponseEntity<?> deleteInte( Long id){

        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        repo.delete(planningMaintenance.get());

        return  new ResponseEntity<>("L'intervention est bien supprimé" , HttpStatus.OK);
    }



    public List<InterventionDTO> getAll() {
        List<Intervention> interventions = repo.findAll();
        for (Intervention intervention : interventions) {
            String idEqui = intervention.getId_Techn();
            if (idEqui == null) {
                intervention.setTechnicien(null);
                continue;
            }
            TechnicienDTO equipe = erepo.getUser(idEqui);
            if (equipe == null) {
                intervention.setTechnicien(null);
            } else {
                intervention.setTechnicien(equipe);
            }

            Equipement equipement = eqRepo.getEquiById(intervention.getId_Equipement());
            if (equipement == null) {
                intervention.setEquipement(null);
            } else {
                intervention.setEquipement(equipement);
            }
            repo.save(intervention);
        }
        List<InterventionDTO> interventionDTOs = interventions.stream()
                .map(mp::from)
                .collect(Collectors.toList());
        return interventionDTOs;
    }

    public ResponseEntity<?> findByStatus(statusIntervention status){
        return  new ResponseEntity<>(repo.findByStatus(status).stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?>  getStatusOfInter( Long id){
        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(planningMaintenance.get().getStatus(), HttpStatus.OK);
    }

    public ResponseEntity<?>  updateStatusOfInter( Long id , statusIntervention st){
        Optional<Intervention> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        planningMaintenance.get().setStatus(st);
        repo.save(planningMaintenance.get());

        return  new ResponseEntity<>("Le status est bien modifie", HttpStatus.OK);
    }


    public List<InterventionDTO> getInterventionOfPlanning( Long idPl){
        return repo.findById_Planning(idPl).stream().map(mp::from).collect(Collectors.toList());
    }


    public List<InterventionDTO> getInterventionOfEquipe( Long idEquipe){
        return repo.findById_Equipe(idEquipe).stream().map(mp::from).collect(Collectors.toList());
    }

    public ResponseEntity<?> attribuerEquipeToIntervention(Long idInte , String idEqui ){

        Optional<Intervention> intervention = repo.findById(idInte);
        if (!intervention.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+idInte , HttpStatus.NOT_FOUND);
        }

        TechnicienDTO e = erepo.getUser(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Techn(idEqui);
        intervention.get().setTechnicien(e);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien attribue à l'interevntion: "+idInte , HttpStatus.OK);


    }

    public ResponseEntity<?> dissosierEquipeToIntervention(Long idInte , Long idEqui ){

        Optional<Intervention> intervention = repo.findById(idInte);
        if (!intervention.isPresent()){

            return new ResponseEntity<>("Aucune intervention n'est trouvé avec ce id: "+idInte , HttpStatus.NOT_FOUND);
        }

        TechnicienDTO e = erepo.findTechnicienById(idEqui);
        if(e==null){
            return new ResponseEntity<>("Aucune équipe n'est trouvé avec ce id: "+idEqui , HttpStatus.NOT_FOUND);
        }

        intervention.get().setId_Techn(null);
        intervention.get().setTechnicien(null);
        repo.save(intervention.get());
        return new ResponseEntity<>("L'quipe est bien dessocier  de l'interevntion: "+idInte , HttpStatus.OK);


    }

    public List<InterventionDTO> getInterventionOfEqui(Long idEqui){

         List<Intervention> interventions = repo.findById_Equipement(idEqui);
         for(Intervention i :interventions){
              Equipement eq = eqRepo.getEquiById(i.getId_Equipement());
              i.setEquipement(eq);
              TechnicienDTO tech = erepo.getUser(i.getId_Techn());
              i.setTechnicien(tech);
         }
         if(interventions.size()==0){
             System.out.println("intervention size"+interventions.size());
         }
        if(interventions.size()!=0){
            System.out.println("voila les interventions"+interventions);
        }

         return  interventions.stream().map(mp::from).collect(Collectors.toList());
    }

    // Exemple de méthode de filtrage par type dans InterventionService
    public List<InterventionDTO> getInterventionByType(TypeIntervention type) {
        // Utiliser la méthode correspondante de l'interface repo pour récupérer les données filtrées
        List<Intervention> interventions = repo.findByType(type);
        // Convertir les données en DTO si nécessaire
        List<InterventionDTO> interventionDTOs = interventions.stream().map(mp::from).collect(Collectors.toList());
        return interventionDTOs;
    }


    public  List<InterventionDTO> getInterventionsOfTehnicien1(String id){
        List<Intervention> interventions = repo.findAll();
        List<InterventionDTO> in = new ArrayList<>() ;
        for( Intervention i :interventions){
             if(i.getId_Techn().equals(id) ){

                 in.add(mp.from(i));
             }

        }


        return in;

    }


    public  List<InterventionDTO> getInterventionsOfTehnicien(String id){
        List<Intervention> interventions = repo.findAll();
        for (Intervention intervention : interventions) {
            String idEqui = intervention.getId_Techn();
            if (idEqui == null) {
                intervention.setTechnicien(null);
                continue;
            }
            TechnicienDTO equipe = erepo.getUser(idEqui);
            if (equipe == null) {
                intervention.setTechnicien(null);
            } else {
                System.out.println("tech"+equipe);
                intervention.setTechnicien(equipe);
            }

            Equipement equipement = eqRepo.getEquiById(intervention.getId_Equipement());
            if (equipement == null) {
                intervention.setEquipement(null);
            } else {
                System.out.println("equi"+equipement);
                intervention.setEquipement(equipement);
            }
            repo.save(intervention);
        }
        List<InterventionDTO> interventionDTOs = interventions.stream()
                .map(mp::from)
                .collect(Collectors.toList());


        List<InterventionDTO> interventionOfTechnicen= new ArrayList<>();
        for(InterventionDTO i : interventionDTOs){
            System.out.println("inter"+i);
            System.out.println("idtech"+ i.getId_Techn());

             if( i.getId_Techn().equals(id)){
                 interventionOfTechnicen.add(i);

             }
        }

        for(InterventionDTO l :interventionOfTechnicen){
            System.out.println("intevrention od tech"+l);
        }
        return interventionOfTechnicen;

    }
   public  List<Intervention> getInterventionNormal(String pr){
        List<Intervention> interventions = repo.findAll();
        List<Intervention>dt= new ArrayList<>();
        for(Intervention i :interventions){
            if(i.getPriorite().equals(pr)){

                //InterventionDTO in = mp.from(i);
                dt.add(i);
            }
        }
        return  dt;
    }


    public  List<InterventionDTO> getInterventionByStatus(statusIntervention type){
        return    repo.findByStatus(type).stream().map(mp::from).collect(Collectors.toList());
    }


    public  List<InterventionDTO> getInterventionByPriorite(PrioriteEnum type){
        return    repo.findByPriorite(type).stream().map(mp::from).collect(Collectors.toList());
    }
}
