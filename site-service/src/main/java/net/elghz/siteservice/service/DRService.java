package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.DC;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.exception.NotFoundException;
import net.elghz.siteservice.mapper.CTMapper;
import net.elghz.siteservice.mapper.drMapper;
import net.elghz.siteservice.repository.CTRepo;
import net.elghz.siteservice.repository.DCRepo;
import net.elghz.siteservice.repository.DRRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DRService {

    private DRRepo repo;
    private drMapper ctMapper;
    public Optional<DRDTO> getCatId(Long id) throws NotFoundException {

        Optional<DR>  eq = repo.findById(id);
        if(eq.isPresent()) {
            DRDTO equipementDTO = ctMapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new NotFoundException("Aucune DR avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<DRDTO> allCategories(){
        return repo.findAll().stream().map(ctMapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<DR> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(DRDTO a){
        String name = a.getName();
        Optional<DR> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            DR al = ctMapper.from(a);
            repo.save(al);
            return true;}
    }

    public List<DRDTO> addCentreTechniques(List<DRDTO> DRDTOList) {
        List<DR> newCentreTechniques = new ArrayList<>();

        for (DRDTO DRDTO : DRDTOList) {
            DR newDR = ctMapper.from(DRDTO);
            newCentreTechniques.add(newDR);
        }

        List<DR> savedCentreTechniques = repo.saveAll(newCentreTechniques);
        return savedCentreTechniques.stream().map(ctMapper::from).collect(Collectors.toList());
    }

    public boolean updateCat(DRDTO updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<DR> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            DR dtoe= existingAttrOptional.get();

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

}
