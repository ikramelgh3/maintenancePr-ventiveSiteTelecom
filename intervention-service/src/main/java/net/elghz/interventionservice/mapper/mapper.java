package net.elghz.interventionservice.mapper;

import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class mapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public InterventionDTO from (Intervention e){
        return modelMapper.map(e, InterventionDTO.class);
    }

    public Intervention from (InterventionDTO   attrDTO){
        return modelMapper.map(attrDTO,Intervention.class);
    }
}
