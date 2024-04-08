package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.exception.AttributeNotFoundException;
import net.elghz.siteservice.mapper.attributeMapper;
import net.elghz.siteservice.repository.attributeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class attributeService {

    private attributeRepo repo;
    private attributeMapper amapper;

    public Optional <attributeDTO> getAttrId(Long id) throws AttributeNotFoundException {

        Optional<Attribute>  eq = repo.findById(id);
        if(eq.isPresent()) {
            attributeDTO equipementDTO = amapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new AttributeNotFoundException("Aucune attribut avec ce id :" +id);
        }

    }
    public List<attributeDTO> allAttribute(){
        return repo.findAll().stream().map(amapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<Attribute> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addAttribute(attributeDTO a){
        String name = a.getName();
        Optional<Attribute> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            Attribute al = amapper.from(a);
            repo.save(al);
            return true;}
    }

    public boolean updateAttribute(attributeDTO updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<Attribute> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            Attribute dtoe= existingAttrOptional.get();
            amapper.update(updatedAttribute , dtoe );

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }



}
