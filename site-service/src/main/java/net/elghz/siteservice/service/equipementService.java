package net.elghz.siteservice.service;

import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.typeEquipementDTO;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.enumeration.Statut;
import net.elghz.siteservice.exception.EquipementNotFoundException;
import net.elghz.siteservice.feign.checklistRestClient;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.repository.SalleRepo;
import net.elghz.siteservice.repository.equipementRepo;
import net.elghz.siteservice.repository.typeEquipementRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private typeEquipementRepo typeRepo;
    @Autowired private SalleRepo srepo;

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
        List<equipementDTO> dto = new ArrayList<>();
        List<equipement> equipements = repo.findAll(); // Récupérer tous les équipements
            for(equipement e :equipements){
                dto.add(mapperEqui.from(e));
            }
        return dto;
    }

    public List<typeEquipementDTO> getTypeEqui(){
        return typeRepo.findAll().stream().map(mapperEqui::from).collect(Collectors.toList());
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



    public equipementDTO addEquip(equipementDTO a , Long id , Long idS  ){

        typeEquipement t= typeRepo.findById(id).get();
        salle s = srepo.findById(idS).get();
        String name = a.getNom();
        String num =a.getNumeroSerie();
        String code = a.getCode();
        Optional<equipement> aa = repo.findByNumeroSerieOrCode( num, code);
        if(aa.isPresent()){
            return null;
        }
        else {
            equipement dto = mapperEqui.from(a);

            dto.setTypeEquipementt(t);
            dto.setSalle(s);
            repo.save(dto);
            return mapperEqui.fromEquipement(dto);}
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

    public Statut etatEquipement(String name){
        Optional<equipement> r =repo.findByNom(name);
        if(r.isPresent()){
            return r.get().getStatut();
        }
        else
            return null;
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
    public equipementDTO updateEquipement(Long id, equipement updatedSite , Long idType, Long idSalle) {
        // Vérifiez d'abord si le numéro de série ou le code de l'équipement mis à jour est déjà présent dans la base de données
        Optional<equipement> existingEquipementByNumeroSerie = repo.findByNumeroSerie(updatedSite.getNumeroSerie());
        Optional<equipement> existingEquipementByCode = repo.findByCode(updatedSite.getCode());

        // Vérifiez si un équipement avec le même numéro de série existe déjà
        if (existingEquipementByNumeroSerie.isPresent() && !existingEquipementByNumeroSerie.get().getId().equals(id)) {
            throw new RuntimeException("Un équipement avec le même numéro de série existe déjà dans la base de données.");
        }

        // Vérifiez si un équipement avec le même code existe déjà
        if (existingEquipementByCode.isPresent() && !existingEquipementByCode.get().getId().equals(id)) {
            throw new RuntimeException("Un équipement avec le même code existe déjà dans la base de données.");
        }

        equipement existingEquipement = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipement introuvable avec l'ID : " + id));

        salle s  = srepo.findById(idSalle).get();
        typeEquipement type = typeRepo.findById(idType).get();
        existingEquipement.setCode(updatedSite.getCode());
        existingEquipement.setNom(updatedSite.getNom());
        existingEquipement.setDateMiseService(updatedSite.getDateMiseService());
        existingEquipement.setTypeEquipementt(type);
        existingEquipement.setDescreption(updatedSite.getDescreption());
        existingEquipement.setStatut(updatedSite.getStatut());
        existingEquipement.setSalle(s);
        existingEquipement.setMarque(updatedSite.getMarque());
        existingEquipement.setNumeroSerie(updatedSite.getNumeroSerie());
        repo.save(existingEquipement);
        return mapperEqui.fromEquipement(existingEquipement);
    }

    public  Boolean checkIfEquipementIsHorsService(Long id){
        equipement e = repo.findById(id).get();
        if(e.getStatut().equals("Hors service")){
            return  true;
        }
        else {
            return false;
        }
    }

    public typeEquipementDTO findByName(String name){
         typeEquipement t= typeRepo.findByName(name).get();
         return  mapperEqui.from(t);
    }

}