package net.elghz.planningmaintenance.service;

import lombok.AllArgsConstructor;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.feign.InterventionRestClient;
import net.elghz.planningmaintenance.feign.ResponsableMaintRestClient;
import net.elghz.planningmaintenance.feign.SiteRestClient;
import net.elghz.planningmaintenance.mapper.mapper;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.model.ResponsableMaint;
import net.elghz.planningmaintenance.model.Site;
import net.elghz.planningmaintenance.repository.planningRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class planningService {

    private planningRepo repo;
    private mapper mp;
    private SiteRestClient srepo;
    private ResponsableMaintRestClient rrepo;
    private InterventionRestClient irepo;

    public ResponseEntity<?> addPlanning(PlanningMaintenanceDTO pl) {
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()) {
          //  pln.setStatus(PlanningStatus.EN_ATTENTE_VALIDATION);
            repo.save(pln);
            return new ResponseEntity<>("Le planning est bien ajouté.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Il existe déjà un planning avec ce nom.", HttpStatus.OK);
    }


    public ResponseEntity<?> addPlanningComplet(PlanningMaintenanceDTO pl ,Long idRespo ,Long idSite  ){
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){

            Site site = srepo.findType(idSite);
            pln.setSite(site);
            pln.setId_Site(idSite);
            pln.setId_Respo(idRespo);
            pln.setResponsableMaint(rrepo.findById(idRespo));
           // pln.setStatus(PlanningStatus.EN_ATTENTE_VALIDATION);
            repo.save(pln);
            return new ResponseEntity<>("Le planning est bien ajouté." , HttpStatus.OK);
        }


        return  new ResponseEntity<>("Il exist déja un planning avec ce nom." , HttpStatus.OK);
    }


    public ResponseEntity<?> findPlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        PlanningMaintenanceDTO  dto = mp.from(planningMaintenance.get());

        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }


    public PlanningMaintenanceDTO findPlanningById( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

          throw  new RuntimeException("Aucune planning n'est trouvé avec ce id: "+id);
        }

        PlanningMaintenanceDTO  dto = mp.from(planningMaintenance.get());

        return  dto;
    }
    public ResponseEntity<?> deletePlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        repo.delete(planningMaintenance.get());

        return  new ResponseEntity<>("Le planning est bien supprimé" , HttpStatus.OK);
    }



    public ResponseEntity<?> getAll(){
        List<PlanningMaintenance> planningMaintenances = repo.findAll();
        List<PlanningMaintenanceDTO> planningMaintenancesDtos = planningMaintenances.stream().map(mp::from).collect(Collectors.toList());
        for( PlanningMaintenanceDTO dto :planningMaintenancesDtos){

            Long idRespo = dto.getId_Respo();
            ResponsableMaint r = rrepo.findById(idRespo);
            dto.setResponsableMaint(r);

            Long idSite = dto.getId_Site();
            Site s = srepo.findType(idSite);
            dto.setSite(s);

            List<Intervention> i = irepo.getInterventionsOfPlanning(dto.getId());
            dto.setInterventionList(i);


        } if(planningMaintenancesDtos.size()!=0){
            return new ResponseEntity<>(planningMaintenancesDtos, HttpStatus.OK);}
        else{
            return new ResponseEntity<>("Aucune planning n'est trouvé", HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<?> findByStatus(PlanningStatus status){
         return  new ResponseEntity<>(repo.findByStatus(status).stream().map(mp::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?>  getStatusOfPlanning( Long id){
        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(planningMaintenance.get().getStatus(), HttpStatus.OK);
    }

    public ResponseEntity<?>  updateStatusOfPlanning( Long id , PlanningStatus st){
        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }
        planningMaintenance.get().setStatus(st);
        repo.save(planningMaintenance.get());

        return  new ResponseEntity<>("Le status est bien modifie", HttpStatus.OK);
    }

    public List<PlanningMaintenanceDTO> getPlanningsOfSite(Long idSite){
        return repo.findById_Site(idSite).stream().map(mp::from).collect(Collectors.toList());
    }


    public List<PlanningMaintenanceDTO> getPlanningsOfResp(Long idResp) {
        return repo.findById_Respo(idResp).stream().map(mp::from).collect(Collectors.toList());
    }


    public ResponseEntity<?>addInterventionToPlanning(Long idPlanning, Intervention intervention){
        Optional<PlanningMaintenance> planningMaintenance = repo.findById(idPlanning);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+idPlanning , HttpStatus.NOT_FOUND);
        }

        List<Intervention> interventionList = planningMaintenance.get().getInterventionList();
        for(Intervention i : interventionList){
            if(i.getName().equals(intervention.getName())){
                return new ResponseEntity<>("L'intervention est deja associe au planning", HttpStatus.OK);
            }
        }
        interventionList.add(intervention);
        repo.save(planningMaintenance.get());
        Boolean me =irepo.addInterToPlanning(idPlanning,intervention);
        if(me==true){
            return new ResponseEntity<>("L'intervention est bien ajouté au planning " , HttpStatus.OK);

        }
        if(me==false){
            return new ResponseEntity<>("L'intervention existe deja " , HttpStatus.OK);
        }
        return new ResponseEntity<>("L'intervention est bien ajouté au planning " , HttpStatus.OK);
    }


    public List<Intervention> getInterventionOfPlanning(Long idPl){
        return irepo.getInterventionsOfPlanning(idPl);
    }


    //si tous les intervention sont realise alors le planning est bien etabli cad ferme

}
