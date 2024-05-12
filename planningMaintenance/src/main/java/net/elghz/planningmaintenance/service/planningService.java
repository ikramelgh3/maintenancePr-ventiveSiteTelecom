package net.elghz.planningmaintenance.service;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.planningmaintenance.dto.PlanningMaintenanceDTO;
import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.exception.PlanningNameExistsException;
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
import java.util.ArrayList;
import java.util.Date;
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

    public PlanningMaintenanceDTO addPlanning(PlanningMaintenanceDTO pl) {
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()) {
          //  pln.setStatus(PlanningStatus.EN_ATTENTE_VALIDATION);
            pln.setStatus(PlanningStatus.EN_ATTENTE);
            pln.setDateCreation(new Date());
            repo.save(pln);
            return pl;

        }
        return  null;
    }

    public int getSize(){
         return repo.findAll().size();
    }
    public PlanningMaintenance updatePlanning(Long id, PlanningMaintenanceDTO planningDTO , Long site, Long resp) throws PlanningNameExistsException {
        Optional<PlanningMaintenance> optionalPlanning = repo.findById(id);
        if (optionalPlanning.isPresent()) {
            PlanningMaintenance existingPlanning = optionalPlanning.get();
            if (repo.existsByNameAndIdIsNot(planningDTO.getName(), id)) {
                System.out.println(planningDTO.getName());
                throw new PlanningNameExistsException("Un planning avec ce nom existe déjà");
            }
            System.out.println(planningDTO.getName());
            existingPlanning.setName(planningDTO.getName());
            existingPlanning.setDateDebutRealisation(planningDTO.getDateDebutRealisation());
            existingPlanning.setDateFinRealisation(planningDTO.getDateFinRealisation());
            existingPlanning.setDescription(planningDTO.getDescription());
            existingPlanning.setSemestre(planningDTO.getSemestre());
            existingPlanning.setId_Respo(resp);
            existingPlanning.setId_Site(site);
            System.out.println(site);
            System.out.println(resp);

            return repo.save(existingPlanning);
        } else {
            throw new NotFoundException("Planning not found with id: " + id);
        }
    }
    public boolean checkPlanningExists(String name) {
        return repo.existsByName(name);
    }

    public  List<PlanningMaintenanceDTO> findBySemestre(String semestre){
         return  repo.findBySemestre(semestre).stream().map(mp::from).collect(Collectors.toList());
    }

    public PlanningMaintenanceDTO addPlanningComplet(PlanningMaintenanceDTO pl ,Long idRespo ,Long idSite  ){
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){

            Site site = srepo.findType(idSite);
            pln.setSite(site);
            pln.setStatus(PlanningStatus.EN_ATTENTE);
            pln.setId_Site(idSite);
            pln.setId_Respo(idRespo);
            pln.setResponsableMaint(rrepo.findById(idRespo));
            pln.setDateCreation(new Date());
           // pln.setStatus(PlanningStatus.EN_ATTENTE_VALIDATION);
            repo.save(pln);
            return pl;
        }


        return  null;
    }


    public PlanningMaintenanceDTO addPlanningCom(PlanningMaintenanceDTO pl ,Long idRespo  ){
        PlanningMaintenance pln = mp.from(pl);
        Optional<PlanningMaintenance> planningMaintenance = repo.findByName(pln.getName());
        if (!planningMaintenance.isPresent()){
            pln.setId_Respo(idRespo);
            pln.setResponsableMaint(rrepo.findById(idRespo));
            // pln.setStatus(PlanningStatus.EN_ATTENTE_VALIDATION);
            repo.save(pln);
           return  pl;
        }


        return  null;
    }


    public ResponseEntity<?> findPlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);
        if (!planningMaintenance.isPresent()){

            return new ResponseEntity<>("Aucune planning n'est trouvé avec ce id: "+id , HttpStatus.NOT_FOUND);
        }

        PlanningMaintenanceDTO  dto = mp.from(planningMaintenance.get());

        return  new ResponseEntity<>(dto , HttpStatus.OK);
    }

    public PlanningMaintenanceDTO findPlanningByIdWithDetails(Long id) {
        Optional<PlanningMaintenance> planningOptional = repo.findById(id);

        if (planningOptional.isPresent()) {
            PlanningMaintenance planningMaintenance = planningOptional.get();
            PlanningMaintenanceDTO dto = mp.from(planningMaintenance);

            // Récupérer et définir le responsable associé au planning
            Long idRespo = dto.getId_Respo();
            ResponsableMaint responsable = rrepo.findById(idRespo);
            dto.setResponsableMaint(responsable);

            // Récupérer et définir le site associé au planning
            Long idSite = dto.getId_Site();
            Site site = srepo.findType(idSite);
            dto.setSite(site);

            // Récupérer et définir les interventions associées au planning
            List<Intervention> interventions = irepo.getInterventionsOfPlanning(dto.getId());
            dto.setInterventionList(interventions);

            return dto;
        } else {
            throw new RuntimeException("Aucun planning trouvé avec l'ID : " + id);
        }
    }

    public PlanningMaintenanceDTO findPlanningById( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);

        if (!planningMaintenance.isPresent()){

          throw  new RuntimeException("Aucune planning n'est trouvé avec ce id: "+id);
        }
        PlanningMaintenanceDTO  dto = mp.from(planningMaintenance.get());
        Long idS = dto.getId_Site();
        Site s = srepo.findType(idS);
        dto.setSite(s);
        return  dto;
    }
    public void deletePlanning( Long id){

        Optional<PlanningMaintenance> planningMaintenance = repo.findById(id);


        repo.delete(planningMaintenance.get());


    }



    public List<PlanningMaintenanceDTO> getAll( ){
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
           return planningMaintenancesDtos;}
        else{
            return null;
        }
    }


    public List<PlanningMaintenanceDTO> findByStatus(PlanningStatus status){

        List<PlanningMaintenanceDTO> dto=  repo.findByStatus(status).stream().map(mp::from).collect(Collectors.toList());
        for(PlanningMaintenanceDTO d :dto){
            Site s = srepo.findType(d.getId_Site());
            ResponsableMaint a = rrepo.findById(d.getId_Respo());
            d.setSite(s);
            d.setResponsableMaint(a);
        }
         return  dto;
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

    public boolean existsById(Long id) {

        Optional<PlanningMaintenance> pl  = repo.findById(id);
        if(pl.isPresent()){
            return true;
        }
        else
            return false;
    }


    public List<PlanningMaintenanceDTO> planningByTypeSite(String type){
        List<Site> sites= srepo.getSitesByType(type);
        List<PlanningMaintenanceDTO> pl = new ArrayList<>();
        for(Site s :sites){
           List<PlanningMaintenanceDTO>  dt = repo.findById_Site(s.getId()).stream().map(mp::from).collect(Collectors.toList());
           for(PlanningMaintenanceDTO d :dt){
               d.setResponsableMaint(rrepo.findById(d.getId_Respo()));
               d.setSite(srepo.findType(d.getId_Site()));
               pl.add(d);

           }

        }
        return pl;

    }

    public List<PlanningMaintenanceDTO> findByKeyword(String keyword){
         return  repo.findAllPln(keyword).stream().map(mp::from).collect(Collectors.toList());
    }



    public List<Intervention> interventionOfSite(Long idSITE){
        List<PlanningMaintenance> planningMaintenances = repo.findById_Site(idSITE);
        List<Intervention> interventions = new ArrayList<>();
        for(PlanningMaintenance pl :planningMaintenances){
            List <Intervention> in = irepo.getInterventionsOfPlanning(pl.getId());
            for(Intervention i : in){
                 interventions.add(i);
            }


        }
        return  interventions;
    }



    public int getNbrePlanning(){
         return repo.findAll().size();
    }

    //une fois une intervention est realise donc le status de planning est en cours
    //si tous les intervention sont realise alors le planning est bien etabli cad ferme

}
