package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class attributeMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    private categorieMapper categorieMapper;

    public attributeDTO from (Attribute e){
        return modelMapper.map(e, attributeDTO.class);
    }

    public Attribute from (attributeDTO   attrDTO){
        return modelMapper.map(attrDTO,Attribute.class);
    }

    public void update(attributeDTO attributeDTO, Attribute attribute) {
        attribute.setName(attributeDTO.getName());
        attribute.setAttributeValue(attributeDTO.getAttributeValue());
        // Mise à jour de la catégorie
        attribute.setCategorie(categorieMapper.from(attributeDTO.getCategorie()));
    }

    public List<Attribute> fromList(List<attributeDTO> attributeDTOList) {
        List<Attribute> attributeList = new ArrayList<>();
        for (attributeDTO dto : attributeDTOList) {
            attributeList.add(from(dto));
        }
        return attributeList;
    }

}
