package net.elghz.checklistservice.mapper;

import net.elghz.checklistservice.dtos.ChecklistDTO;
import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class mapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public ChecklistDTO from (Checklist e){
        return modelMapper.map(e, ChecklistDTO.class);
    }

    public Checklist from (ChecklistDTO   attrDTO){
        return modelMapper.map(attrDTO,Checklist.class);
    }

    public PointMesureDTO from (PointMesure e){
        return modelMapper.map(e, PointMesureDTO.class);
    }

    public PointMesure from (PointMesureDTO   attrDTO){
        return modelMapper.map(attrDTO,PointMesure.class);
    }

//    public void update(ChecklistDTO attributeDTO, Checklist attribute) {
//        attribute.setDescription(attributeDTO.getDescription());
//
//    }
/*

    public void updatePointMesure(PointMesureDTO attributeDTO, PointMesure attribute) {
        attribute.setAttribut(attributeDTO.getAttribut());
        attribute.setValue(attributeDTO.getValue());

    }
*/
}
