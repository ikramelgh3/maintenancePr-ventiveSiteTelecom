package net.elghz.anomalieservice.mapper;

import net.elghz.anomalieservice.dto.AnomalieDTO;
import net.elghz.anomalieservice.dto.PhotoAnomalieDTO;
import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.entities.PhotoAnomalie;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class mapper {

    private ModelMapper modelMapper = new ModelMapper();

    public mapper() {
        this.modelMapper = new ModelMapper();
        configureMappings();
    }

    private void configureMappings() {
        modelMapper.addMappings(new PropertyMap<Anomalie, AnomalieDTO>() {
            @Override
            protected void configure() {
                map().setId_TechnicienD(source.getId_TechnicienD());
                map().setTechnicien(source.getTechnicienDetecteur());
            }
        });

        modelMapper.addMappings(new PropertyMap<AnomalieDTO, Anomalie>() {
            @Override
            protected void configure() {
                map().setId_TechnicienD(source.getId_TechnicienD());
                map().setTechnicienDetecteur(source.getTechnicien());
            }
        });
    }
    public PhotoAnomalieDTO from (PhotoAnomalie e){
        return modelMapper.map(e, PhotoAnomalieDTO.class);
    }

    public PhotoAnomalie from (PhotoAnomalieDTO   attrDTO){
        return modelMapper.map(attrDTO, PhotoAnomalie.class);
    }

    public AnomalieDTO from (Anomalie e){
        return modelMapper.map(e, AnomalieDTO.class);
    }

    public Anomalie from (AnomalieDTO   attrDTO){
        return modelMapper.map(attrDTO,Anomalie.class);
    }



}
