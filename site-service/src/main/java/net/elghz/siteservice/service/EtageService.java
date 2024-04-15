package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.etageDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.etage;
import net.elghz.siteservice.entities.immuble;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.CTRepo;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.EtageRepo;
import net.elghz.siteservice.repository.ImmubleRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class EtageService {

    private EtageRepo repo;

    private ImmubleRepo dcRepo;

    private siteMapper smapper;



    public Optional<etageDTO> getCatId(Long id) throws NotFoundException {

        Optional<etage>  eq = repo.findById(id);
        if(eq.isPresent()) {
            etageDTO equipementDTO = smapper.from(eq.get());
            return Optional.ofNullable(equipementDTO);
        }
        else{
            throw  new NotFoundException("Aucune etage avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<etageDTO> allCategories(){
        return repo.findAll().stream().map(smapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<etage> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(etageDTO a){
        int name = a.getNumeroEtage();
        Optional<etage> aa = repo.findByNumeroEtage(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            etage al = smapper.from(a);
            repo.save(al);
            return true;
        }
    }
    public List<etage> addCentreTechniques(List<etageDTO> centreTechniqueDTOList) {
        List<etage> newCentreTechniques = new ArrayList<>();

        for (etageDTO centreTechniqueDTO : centreTechniqueDTOList) {
            etage newCentreTechnique = smapper.from(centreTechniqueDTO);
            newCentreTechniques.add(newCentreTechnique);
        }

        List<etage> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques;
    }
    public boolean updateCat(etage updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<etage> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            etage dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

    public void associateEtagetoImmuble(Long ctId, Long dcId) throws NotFoundException {
        etage centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Etage non trouvé avec l'ID : " + ctId));

            immuble dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Immuble non trouvé avec l'ID : " + dcId));

        centreTechnique.setImmuble(dc);
        repo.save(centreTechnique);
    }

    public void disassociateCTfromDC(Long ctId) throws NotFoundException {
        etage centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("etage non trouvé avec l'ID : " + ctId));

        centreTechnique.setImmuble(null);
        repo.save(centreTechnique);
    }

    //etage existe dans immuble
    public List<etageDTO> getEtageByImmuble(Long dcId) throws NotFoundException {
        immuble dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Immuble non trouvé avec l'ID : " + dcId));

        List<etageDTO> centreTechniques = repo.findByImmuble(dc).stream().map(smapper::from).collect(Collectors.toList());
        if (centreTechniques.isEmpty()) {
            throw new NotFoundException("Aucun centre technique associé à: "+ dc.getName());
        }

        return centreTechniques;
    }

    public immuble getDCFromCT(int nm){
        return repo.findByNumeroEtage(nm).get().getImmuble();
    }

}

