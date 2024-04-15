package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.salleDTO;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.mapper.equipementMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class SalleService {

    private SalleRepo repo;
    private EtageRepo dcRepo;

    private siteMapper smapper;
    private equipementMapper eqmapper;
    private equipementRepo eqrepo;
    public Optional<salleDTO> getCatId(Long id) throws NotFoundException {

        Optional<salle>  eq = repo.findById(id);
        if(eq.isPresent()) {
            salleDTO equipementDTO = smapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new NotFoundException("Aucune Salle avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<salleDTO> allCategories(){
        return repo.findAll().stream().map(smapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<salle> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(salleDTO a){
        int num = a.getNumeroSalle();
        Optional<salle> aa = repo.findByNumeroSalle(num);
        if(aa.isPresent()){
            return false;
        }
        else {
            salle al = smapper.from(a);
            repo.save(al);
            return true;}
    }
    public List<salle> addCentreTechniques(List<salleDTO> centreTechniqueDTOList) {
        List<salle> newCentreTechniques = new ArrayList<>();

        for (salleDTO centreTechniqueDTO : centreTechniqueDTOList) {
            salle newCentreTechnique = smapper.from(centreTechniqueDTO);
            newCentreTechniques.add(newCentreTechnique);
        }

        List<salle> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques;
    }
    public boolean updateCat(salle updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<salle> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            salle dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

    public void associateCTtoDC(Long ctId, Long dcId) throws NotFoundException {
        salle centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Salle non trouvé avec l'ID : " + ctId));

        etage dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Etage non trouvé avec l'ID : " + dcId));

        centreTechnique.setEtage(dc);
        repo.save(centreTechnique);
    }

    public void disassociateCTfromDC(Long ctId) throws NotFoundException {
        salle centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Salle non trouvé avec l'ID : " + ctId));

        centreTechnique.setEtage(null);
        repo.save(centreTechnique);
    }

    public List<salleDTO> getCTsByDC(Long dcId) throws NotFoundException {
        etage dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Etage non trouvé avec l'ID : " + dcId));

        List<salleDTO> centreTechniques = repo.findByEtage(dc).stream().map(smapper::from).collect(Collectors.toList());;
        if (centreTechniques.isEmpty()) {
            throw new NotFoundException("Aucun etage est associé à: "+ dc.getNumeroEtage());
        }

        return centreTechniques;
    }


    public int getDCByCTId(Long ctId) throws NotFoundException {
        Optional<salle> optionalCT = repo.findById(ctId);
        if (optionalCT.isPresent()) {
            salle ct = optionalCT.get();
            return ct.getEtage().getNumeroEtage();
        } else {
            throw new NotFoundException("Salle non trouvé avec l'ID : " + ctId);
        }
    }

    public etage getDCFromCT(int id){
        return repo.findByNumeroSalle(id).get().getEtage();
    }


    //ajouterequipementToSalle

    public void addEquipementToSalle(Long idEq , Long salleId) throws NotFoundException {

        salle centreTechnique = repo.findById(salleId)
                .orElseThrow(() -> new NotFoundException("Salle non trouvé avec l'ID : " + salleId));

        equipement dc = eqrepo.findById(idEq)
                .orElseThrow(() -> new NotFoundException("Equipement non trouvé avec l'ID : " + idEq));

        centreTechnique.addEquipement(dc);
        repo.save(centreTechnique);
    }

    public void supprimerEquipementFromSalle(Long idEq,Long ctId) throws NotFoundException {
        salle centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Salle non trouvé avec l'ID : " + ctId));
        equipement dc = eqrepo.findById(idEq)
                .orElseThrow(() -> new NotFoundException("Equipement non trouvé avec l'ID : " + idEq));
        centreTechnique.removePhoto(dc);
        repo.save(centreTechnique);
    }

}

