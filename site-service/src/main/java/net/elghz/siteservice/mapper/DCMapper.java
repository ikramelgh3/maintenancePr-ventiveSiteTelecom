package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.DCDTO;
import net.elghz.siteservice.dtos.attributeDTO;
import net.elghz.siteservice.entities.Attribute;
import net.elghz.siteservice.entities.DC;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DCMapper {

    private final ModelMapper modelMapper = new ModelMapper();


    public DCDTO from (DC e){
        return modelMapper.map(e, DCDTO.class);
    }

    public DC from (DCDTO   attrDTO){
        return modelMapper.map(attrDTO,DC.class);
    }

    public void update(DCDTO attributeDTO, DC attribute) {
        attribute.setName(attributeDTO.getName());
    }


}
