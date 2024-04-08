package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.DRDTO;
import net.elghz.siteservice.dtos.PhotoDTO;
import net.elghz.siteservice.entities.DR;
import net.elghz.siteservice.entities.Photo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class photoMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public PhotoDTO from (Photo e){
        return modelMapper.map(e, PhotoDTO.class);
    }

    public Photo from (PhotoDTO   attrDTO){
        return modelMapper.map(attrDTO,Photo.class);
    }

    public void update(PhotoDTO attributeDTO, Photo attribute) {
        attribute.setName(attributeDTO.getName());

    }



}
