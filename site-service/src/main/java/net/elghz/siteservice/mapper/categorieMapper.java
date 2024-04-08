package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.categorieDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.categorie;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class categorieMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public categorieDTO from(categorie categorie) {
        return modelMapper.map(categorie, categorieDTO.class);
    }

    public categorie from(categorieDTO categorieDTO) {
        return modelMapper.map(categorieDTO, categorie.class);
    }

   /*
    public void update(categorieDTO categorieDTO, categorie categorie) {
        categorie.setName(categorieDTO.getName());

        // Mise à jour des attributs
        List<Attribute> attributes = categorie.getAttributes();
        //List<attributeDTO> updatedAttributes = categorieDTO.getAttributes();

        // Supprimer tous les attributs existants
        attributes.clear();

        // Ajouter les attributs mis à jour
        for (attributeDTO updatedAttribute : updatedAttributes) {
            Attribute attribute = modelMapper.map(updatedAttribute, Attribute.class);
            attributes.add(attribute);
        }
    }*/
}
