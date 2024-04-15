package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.immubleDTO;
import net.elghz.siteservice.entities.*;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class ImmubleService {

    private ImmubleRepo repo;
    private siteMapper smapper;
    private EtageRepo dcRepo;
    private SiteRepository siteRepo;




    public Optional<immubleDTO> getCatId(Long id) throws NotFoundException {

        Optional<immuble>  eq = repo.findById(id);
        if(eq.isPresent()) {
            immubleDTO equipementDTO = smapper.from(eq.get());
            return Optional.ofNullable(equipementDTO);
        }
        else{
            throw  new NotFoundException("Aucune Immuble avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<immubleDTO> allCategories(){
        return repo.findAll().stream().map(smapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<immuble> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(immubleDTO a){
        String name = a.getName();
        Optional<immuble> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            immuble al = smapper.from(a);
            repo.save(al);
            return true;}
    }
    public List<immuble> addImmuble(List<immubleDTO> centreTechniqueDTOList) {
        List<immuble> newCentreTechniques = new ArrayList<>();

        for (immubleDTO centreTechniqueDTO : centreTechniqueDTOList) {
            immuble newCentreTechnique = smapper.from(centreTechniqueDTO);
            newCentreTechniques.add(newCentreTechnique);
        }

        List<immuble> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques;
    }
    public boolean updateCat(immuble updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<immuble> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            immuble dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

    public void associateImmubletoSite(Long ctId, Long dcId) throws NotFoundException {
        immuble centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Immuble non trouvé avec l'ID : " + ctId));

        Site dc = siteRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Site non trouvé avec l'ID : " + dcId));

        centreTechnique.setSite(dc);
        repo.save(centreTechnique);
    }



    public void disassociateImmublefromSite(Long ctId) throws NotFoundException {
        immuble centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Immuble non trouvé avec l'ID : " + ctId));

        centreTechnique.setSite(null);
        repo.save(centreTechnique);
    }



    public List<immubleDTO> getCTsBySite(Long dcId) throws NotFoundException {
        Site dc = siteRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("Site non trouvé avec l'ID : " + dcId));

        List<immubleDTO> centreTechniques = repo.findBySite(dc).stream().map(smapper::from).collect(Collectors.toList());
        if (centreTechniques.isEmpty()) {
            throw new NotFoundException("Aucun immuble associé à: "+ dc.getName());
        }

        return centreTechniques;
    }



    public Site getDCFromCT(String name){
        return repo.findByName(name).get().getSite();
    }

}

