package net.elghz.userservice.mapper;

import net.elghz.userservice.dtos.CompetenceDTO;
import net.elghz.userservice.dtos.TechnicienDTO;
import net.elghz.userservice.dtos.responsableDTO;
import net.elghz.userservice.dtos.utilisateurDTO;
import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.ResponsableMaintenance;
import net.elghz.userservice.entities.Technicien;
import net.elghz.userservice.entities.utilisateur;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class mapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public CompetenceDTO from (Competence e){
        return modelMapper.map(e, CompetenceDTO.class);
    }

    public Competence from (CompetenceDTO   attrDTO){
        return modelMapper.map(attrDTO,Competence.class);
    }

    public TechnicienDTO from (Technicien e){
        return modelMapper.map(e, TechnicienDTO.class);
    }

    public Technicien from (TechnicienDTO   attrDTO){
        return modelMapper.map(attrDTO,Technicien.class);
    }

    public utilisateurDTO from (utilisateur e){
        return modelMapper.map(e, utilisateurDTO.class);
    }

    public utilisateur from (utilisateurDTO   attrDTO){
        return modelMapper.map(attrDTO,utilisateur.class);
    }


    public responsableDTO from (ResponsableMaintenance e){
        return modelMapper.map(e, responsableDTO.class);
    }

    public ResponsableMaintenance from (responsableDTO   attrDTO){
        return modelMapper.map(attrDTO,ResponsableMaintenance.class);
    }

    public void updateCompetence(CompetenceDTO attributeDTO, Competence attribute) {
        attribute.setCompetence(attributeDTO.getCompetence());

    }


}
