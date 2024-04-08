package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.DR;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class drMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public DRDTO from (DR e){
        return modelMapper.map(e, DRDTO.class);
    }

    public DR from (DRDTO   attrDTO){
        return modelMapper.map(attrDTO,DR.class);
    }

    public void update(DRDTO attributeDTO, DR attribute) {
        attribute.setName(attributeDTO.getName());

    }



}
