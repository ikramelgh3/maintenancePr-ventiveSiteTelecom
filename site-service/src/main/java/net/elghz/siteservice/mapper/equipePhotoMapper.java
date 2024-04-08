package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.dtos.PhotoEquipementDTO;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.entities.PhotoEquipement;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class equipePhotoMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public PhotoEquipementDTO from (PhotoEquipement e){
        return modelMapper.map(e, PhotoEquipementDTO.class);
    }

    public PhotoEquipement from (PhotoEquipementDTO   attrDTO){
        return modelMapper.map(attrDTO,PhotoEquipement.class);
    }

    public void update(PhotoEquipementDTO attributeDTO, PhotoEquipement attribute) {
        attribute.setName(attributeDTO.getName());

    }



}
