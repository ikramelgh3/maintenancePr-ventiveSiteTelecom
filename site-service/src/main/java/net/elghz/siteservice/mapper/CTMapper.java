package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.CentreTechniqueDTO;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.CentreTechnique;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CTMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CentreTechniqueDTO from (CentreTechnique e){
        return modelMapper.map(e, CentreTechniqueDTO.class);
    }

    public CentreTechnique from (CentreTechniqueDTO   attrDTO){
        return modelMapper.map(attrDTO,CentreTechnique.class);
    }

    public void update(CentreTechniqueDTO attributeDTO, CentreTechnique attribute) {
        attribute.setName(attributeDTO.getName());

    }



}
