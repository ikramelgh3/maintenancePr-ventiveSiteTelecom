package net.elghz.siteservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.categorieDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.categorie;
import net.elghz.siteservice.exception.AttributeNotFoundException;
import net.elghz.siteservice.mapper.categorieMapper;
import net.elghz.siteservice.repository.CategorieRepo;
import net.elghz.siteservice.repository.attributeRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class categorieService {

    private CategorieRepo repo;
    private categorieMapper catMapper;


    private attributeRepo arepo;
    public Optional<categorieDTO> getCatId(Long id) throws AttributeNotFoundException {

        Optional<categorie>  eq = repo.findById(id);
        if(eq.isPresent()) {
            categorieDTO equipementDTO = catMapper.from(eq.get());
            return Optional.of(equipementDTO) ;
        }
        else{
            throw  new AttributeNotFoundException("Aucune categorie avec ce id :" +id);
        }

    }

    public boolean exists(Long siteId) {
        return repo.existsById(siteId);
    }
    public List<categorieDTO> allCategories(){
        return repo.findAll().stream().map(catMapper::from).collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        Optional<categorie> siteOptional = repo.findById(id);
        if (siteOptional.isPresent()) {
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean addCat(categorieDTO a){
        String name = a.getName();
        Optional<categorie> aa = repo.findByName(name);
        if(aa.isPresent()){
            return false;
        }
        else {
            categorie al = catMapper.from(a);
            repo.save(al);
            return true;}
    }

    public boolean updateCat(categorieDTO updatedAttribute) {
        Long equiId = updatedAttribute.getId();
        Optional<categorie> existingAttrOptional = repo.findById(equiId);

        if (existingAttrOptional.isPresent()) {
            categorie dtoe= existingAttrOptional.get();
         //   catMapper.update(updatedAttribute , dtoe );

            repo.save(dtoe);

            return true;
        } else {
            return false;
        }
    }

    public void addAttributeToCategorie(Long categorieId, Long attributeId) {
        categorie categorie = repo.findById(categorieId)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID : " + categorieId));
        Attribute attribute = arepo.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException("Attribut non trouvé avec l'ID : " + attributeId));
        categorie.getAttributes().add(attribute);
        repo.save(categorie);
    }

    public void removeAttributeFromCategorie(Long categorieId, Long attributeId) {
        categorie categorie = repo.findById(categorieId)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID : " + categorieId));
        Attribute attribute = arepo.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException("Attribut non trouvé avec l'ID : " + attributeId));
        categorie.getAttributes().remove(attribute);
        repo.save(categorie);
    }

    public void ajouterAttributACategorie(Long categoryId, Long attributeId) {
        Optional<categorie> optionalCategorie = repo.findById(categoryId);
        Optional<Attribute> optionalAttribute = arepo.findById(attributeId);

        if (optionalCategorie.isPresent() && optionalAttribute.isPresent()) {
            categorie categorie = optionalCategorie.get();
            Attribute attribute = optionalAttribute.get();
            categorie.addAttribute(attribute);
            repo.save(categorie);
        } else {
            throw new EntityNotFoundException("La catégorie ou l'attribut n'existe pas.");
        }
    }

    public void dissocierAttributDeCategorie(Long categoryId, Long attributeId) {
        Optional<categorie> optionalCategorie = repo.findById(categoryId);
        Optional<Attribute> optionalAttribute = arepo.findById(attributeId);

        if (optionalCategorie.isPresent() && optionalAttribute.isPresent()) {
            categorie categorie = optionalCategorie.get();
            Attribute attribute = optionalAttribute.get();
            categorie.removeAttribute(attribute);
            repo.save(categorie);
        } else {
            throw new EntityNotFoundException("La catégorie ou l'attribut n'existe pas.");
        }
    }

}
