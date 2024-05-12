package net.elghz.siteservice.service;

import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.feign.checklistRestClient;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.model.ChecklistDTO;
import net.elghz.siteservice.repository.equipementRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class equipementService {
    private equipementRepo repo;
    private equipementMapper mapperEqui;
    private checklistRestClient restClient;



    public equipementService ( equipementRepo repo,   equipementMapper mapperEqui){
        this.repo= repo;
        this.mapperEqui= mapperEqui;
    }

    public Optional <equipementDTO> getEquipId(Long id) throws EquipementNotFoundException{

       Optional<equipement>  eq = repo.findById(id);
       if(eq.isPresent()) {
           equipementDTO equipementDTO = mapperEqui.from(eq.get());
           return Optional.of(equipementDTO) ;
       }
       else{
           throw  new EquipementNotFoundException("Aucune equipement avec ce id :" +id);
       }

    }

    public List<equipementDTO> allEquipements() {
        List<equipement> equipements = repo.findAll(); // Récupérer tous les équipements
        equipements.forEach(e -> e.getTypeEquipementt().getName()); // Assurez-vous que les types d'équipement sont chargés
        return equipements.stream().map(mapperEqui::from).collect(Collectors.toList());
    }


    public boolean deleteById(Long id) {
        Optional<equipement> eqOptional = repo.findById(id);
        if (eqOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }



    public boolean addEquip(equipementDTO a){
        String name = a.getNom();
        String num =a.getNumeroSerie();
        Optional<equipement> aa = repo.findByNomOrNumeroSerie(name, num);
        if(aa.isPresent()){
            return false;
        }
        else {
            equipement e = mapperEqui.from(a);
            repo.save(e);
            return true;}
    }

    public boolean updateEqui(equipementDTO updatedequi) {
        Long equiId = updatedequi.getId();
        Optional<equipement> existingEquiOptional = repo.findById(equiId);

        if (existingEquiOptional.isPresent()) {
            equipement dtoe= existingEquiOptional.get();
           mapperEqui.update(updatedequi , dtoe );

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }
    //afficher le statut d'un equipement

    public String etatEquipement(String name){
        Optional<equipement> r =repo.findByNom(name);
        if(r.isPresent()){
            return r.get().getStatut();
        }
        else
            return "Aucune équipement n'existe avec ce nom :" +name;
    }



    public equipementDTO findById(Long id){
        Optional<equipement> opt = repo.findById(id);
        if(!opt.isPresent()){
            throw new RuntimeException("Equipement non trouvé");
        }
        equipement rep = opt.get();
        equipementDTO dt = mapperEqui.from(rep);
        // responsableDTO dt = mp.from(rep);
        return dt;



    }

    public  String localisationOfEquip(Long id){
         equipement e = repo.findById(id).get();
          e.getSalle().getEtage().getImmuble().getSite();

          int salleName = e.getSalle().getNumeroSalle();
          int etape = e.getSalle().getEtage().getNumeroEtage();
          String imm = e.getSalle().getEtage().getImmuble().getName();
          String site = e.getSalle().getEtage().getImmuble().getSite().getName();

          return "Salle : "+salleName + ", Etage : "+ etape+ ", Immuble : "+imm + ", Site : " + site;
    }


}
