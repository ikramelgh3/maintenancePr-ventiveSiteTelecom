package net.elghz.siteservice.service;

import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.repository.equipementRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class equipementService {
    private equipementRepo repo;
    private equipementMapper mapperEqui;

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

    public List<equipementDTO> allEquipements(){
        //List<equipement> e = repo.findAll();
        return repo.findAll().stream().map(mapperEqui::from).collect(Collectors.toList());
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
    //afficher les  sites ou il existe un equipement
    public List<Site> equipementSite(String name) throws EquipementNotFoundException{

        Optional<equipement> optEqui = repo.findByNom(name);
        if(optEqui.isPresent()){
            List <Site> sites = new ArrayList<>();
            sites.add(optEqui.get().getSite());

            return sites;
        }
        else{
            throw  new EquipementNotFoundException("Aucune equipement n'est trouvé avec ce nom: "+ name );
        }
    }




}
