package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.siteDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.mapper.siteMapper;
import net.elghz.siteservice.repository.CTRepo;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class CTService {

    private CTRepo repo;
    private CTMapper ctMapper;
    private DCRepo dcRepo;
     private SiteRepository srepo;
     private siteMapper mp;


     public List<siteDTO> getSite(String name){
          CentreTechnique c =repo.findByName(name).get();
          return c.getSites().stream().map(mp::from).collect(Collectors.toList());

     }


    public void delete(CentreTechnique ct) {
        List<Site> sites = ct.getSites();
        for (Site s : sites) {
            s.setCentreTechnique(null);
            srepo.save(s);
        }
        ct.getSites().clear(); // Dissocier tous les sites du centre technique
        repo.delete(ct);
    }



    public CentreTechniqueDTO getCatId(Long id) throws NotFoundException {

        Optional<CentreTechnique>  eq = repo.findById(id);
        if(eq.isPresent()) {
            CentreTechniqueDTO equipementDTO = ctMapper.from(eq.get());
            return equipementDTO ;
        }
        else{
            return null;
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<CentreTechniqueDTO> allCategories(){
        return repo.findAll().stream().map(ctMapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<CentreTechnique> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(CentreTechniqueDTO a){
        String name = a.getName();
        Optional<CentreTechnique> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            CentreTechnique al = ctMapper.from(a);
            repo.save(al);
            return true;}
    }
    public List<CentreTechniqueDTO> addCentreTechniques(List<CentreTechniqueDTO> centreTechniqueDTOList) {
        List<CentreTechnique> newCentreTechniques = new ArrayList<>();

        for (CentreTechniqueDTO centreTechniqueDTO : centreTechniqueDTOList) {
            CentreTechnique newCentreTechnique = ctMapper.from(centreTechniqueDTO);
            newCentreTechniques.add(newCentreTechnique);
        }

        List<CentreTechnique> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques.stream().map(ctMapper::from).collect(Collectors.toList());
    }
    public boolean updateCat(CentreTechniqueDTO updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<CentreTechnique> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            CentreTechnique dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

    public void associateCTtoDC(Long ctId, Long dcId) throws NotFoundException {
        CentreTechnique centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Centre technique non trouvé avec l'ID : " + ctId));

        DC dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("DC non trouvé avec l'ID : " + dcId));

        centreTechnique.setDc(dc);
        repo.save(centreTechnique);
    }

    public void disassociateCTfromDC(Long ctId) throws NotFoundException {
        CentreTechnique centreTechnique = repo.findById(ctId)
                .orElseThrow(() -> new NotFoundException("Centre technique non trouvé avec l'ID : " + ctId));

        centreTechnique.setDc(null);
        repo.save(centreTechnique);
    }

    public List<CentreTechniqueDTO> getCTsByDC(Long dcId) throws NotFoundException {
        DC dc = dcRepo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("DC non trouvé avec l'ID : " + dcId));

        List<CentreTechnique> centreTechniques = repo.findByDc(dc);
        if (centreTechniques.isEmpty()) {
            throw new NotFoundException("Aucun centre technique associé à: "+ dc.getName());
        }

        return centreTechniques.stream().map(ctMapper::from).collect(Collectors.toList());
    }


    public String getDCByCTId(Long ctId) throws NotFoundException {
        Optional<CentreTechnique> optionalCT = repo.findById(ctId);
        if (optionalCT.isPresent()) {
            CentreTechnique ct = optionalCT.get();
            return ct.getDc().getName();
        } else {
            throw new NotFoundException("Centre technique non trouvé avec l'ID : " + ctId);
        }
    }

    public DC getDCFromCT(String name){
        return repo.findByName(name).get().getDc();
    }



    public CentreTechniqueDTO updateCentreTechnique(Long id, CentreTechnique c){
        CentreTechnique ct = repo.findById(id).get();
        ct.setName(c.getName());
        ct.setDc(c.getDc());
        repo.save(ct);
       return ctMapper.from(ct);
    }


    public boolean checkIfCentreTechniqueExists(String name, Long dcId, Long drId) {
        Optional<CentreTechnique> existingCentreTechnique = repo.findByNameAndDcAndDr(name, dcId, drId);
        return existingCentreTechnique.isPresent();
    }



    public boolean existsById(Long id) {
        return repo.existsById(id);
    }
}

