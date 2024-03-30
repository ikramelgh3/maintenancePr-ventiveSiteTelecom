package net.elghz.siteservice.service;

import lombok.AllArgsConstructor;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.enumeration.SiteType;
import net.elghz.siteservice.repository.attributeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class attributeService {

    private attributeRepo repo;


    public Optional<Attribute> getAttributeId(Long id){
        return  repo.findById(id);
    }

    public List<Attribute> allAttribute(){
        return repo.findAll();
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

    public boolean addAttribute(Attribute a){
        String name = a.getName();
        Optional<Attribute> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            repo.save(a);
            return true;}
    }

    public boolean updateAttribute(Attribute updatedAttribute) {
        Long attributeId = updatedAttribute.getId();
        Optional<Attribute> existingAttributeOptional = repo.findById(attributeId);

        if (existingAttributeOptional.isPresent()) {
            Attribute existingAttribute = existingAttributeOptional.get();
            existingAttribute.setName(updatedAttribute.getName());
            existingAttribute.setAttributeValue(updatedAttribute.getAttributeValue());
            existingAttribute.setAttributeCategory(updatedAttribute.getAttributeCategory());

            repo.save(existingAttribute);

            return true;
        } else {
            return false;
        }
    }


}
