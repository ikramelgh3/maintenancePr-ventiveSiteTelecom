package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.dtos.typeActiviteDTO;
import net.elghz.siteservice.entities.TypeActivite;
import net.elghz.siteservice.entities.equipement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class typeActiviteMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public  TypeActivite f(typeActiviteDTO dto) {
        return modelMapper.map(dto, TypeActivite.class);
    }
    public typeActiviteDTO from (TypeActivite e){
        return modelMapper.map(e, typeActiviteDTO.class);
    }

    public TypeActivite from (typeActiviteDTO equipementDTO){
        return modelMapper.map(equipementDTO,TypeActivite.class);
    }

    public void update (typeActiviteDTO dto , TypeActivite e){
        e.setId(dto.getId());
        e.setName(dto.getName());
    }

}
