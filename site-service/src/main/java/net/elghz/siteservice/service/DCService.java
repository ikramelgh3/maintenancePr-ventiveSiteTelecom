package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.DCMapper;
import net.elghz.siteservice.mapper.drMapper;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.DRRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class DCService {

    private DCRepo repo;
    private DCMapper ctMapper;
    private DRRepo drRepo;
    public Optional<DCDTO> getCatId(Long id) throws NotFoundException {

        Optional<DC>  eq = repo.findById(id);
        if(eq.isPresent()) {
            DCDTO equipementDTO = ctMapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new NotFoundException("Aucune DC avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<DCDTO> allCategories(){
        return repo.findAll().stream().map(ctMapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<DC> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(DCDTO a){
        String name = a.getName();
        Optional<DC> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            DC al = ctMapper.from(a);
            repo.save(al);
            return true;}
    }

    public List<DCDTO> addCentreTechniques(List<DCDTO> DCDTOList) {
        List<DC> newCentreTechniques = new ArrayList<>();

        for (DCDTO DCDTO : DCDTOList) {
            DC newDC = ctMapper.from(DCDTO);
            newCentreTechniques.add(newDC);
        }

        List<DC> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques.stream().map(ctMapper::from).collect(Collectors.toList());
    }

    public boolean updateCat(DCDTO updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<DC> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            DC dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }


    public void associateDCtoDR(Long dcId, Long drId) throws NotFoundException {
        DC dc = repo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("DC non trouvé avec l'ID : " + dcId));

        DR dr = drRepo.findById(drId)
                .orElseThrow(() -> new NotFoundException("DR non trouvé avec l'ID : " + drId));

        dc.setDr(dr);
        repo.save(dc);
    }

    public void disassociateDCfromDR(Long dcId) throws NotFoundException {
        DC dc = repo.findById(dcId)
                .orElseThrow(() -> new NotFoundException("DC non trouvé avec l'ID : " + dcId));

        dc.setDr(null);
        repo.save(dc);
    }

    public List<DC> getDCsByDR(Long drId) throws NotFoundException {
        DR dr = drRepo.findById(drId)
                .orElseThrow(() -> new NotFoundException("DR non trouvé avec l'ID : " + drId));
        return repo.findByDr(dr);
    }

    public String getDRByDC(Long ctId) throws NotFoundException {
        Optional<DC> optionalCT = repo.findById(ctId);
        if (optionalCT.isPresent()) {
            DC ct = optionalCT.get();
            return ct.getDr().getName();
        } else {
            throw new NotFoundException("DC non trouvé avec l'ID : " + ctId);
        }
    }

    public Long DCIdByNamr(String name){
        DC dc= repo.findByName(name).get();
        return  dc.getId();
    }

    public boolean doesDCExist(String dcName) {
        Optional<DC> optionalDC = repo.findByName(dcName);
        return optionalDC.isPresent(); // Retourne true si le DC existe, sinon false
    }
}
