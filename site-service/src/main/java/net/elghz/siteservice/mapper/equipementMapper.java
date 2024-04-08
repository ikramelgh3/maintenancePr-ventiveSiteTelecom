package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.PhotoEquipementDTO;
import net.elghz.siteservice.dtos.equipementDTO;
import net.elghz.siteservice.entities.PhotoEquipement;
import net.elghz.siteservice.entities.Site;
import net.elghz.siteservice.entities.equipement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class equipementMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public equipementDTO from (equipement e){
        return modelMapper.map(e, equipementDTO.class);
    }

    public equipement from (equipementDTO   equipementDTO){
        return modelMapper.map(equipementDTO,equipement.class);
    }
    public equipementDTO fromEquipement(equipement e){

        equipementDTO dto = new equipementDTO();
        BeanUtils.copyProperties(e,dto);
        return dto;
    }

    public  equipement  fromEquipementDto(equipementDTO e){

        equipement equi = new equipement();
        BeanUtils.copyProperties(e,equi);
        return equi;
    }

    public void update(equipementDTO dto, equipement equipement) {
        if (dto.getNom() != null) {
            equipement.setNom(dto.getNom());
        }
        if (dto.getMarque() != null) {
            equipement.setMarque(dto.getMarque());
        }
        if (dto.getDescreption() != null) {
            equipement.setDescreption(dto.getDescreption());
        }
        if (dto.getStatut() != null) {
            equipement.setStatut(dto.getStatut());
        }
        if (dto.getNumeroSerie() != null) {
            equipement.setNumeroSerie(dto.getNumeroSerie());
        }
        if (dto.getDateMiseService() != null) {
            equipement.setDateMiseService(dto.getDateMiseService());
        }
        if (dto.getDateMiseHorsService() != null) {
            equipement.setDateMiseHorsService(dto.getDateMiseHorsService());
        }


        if (dto.getPhotoEquipements() != null) {
            equipement.getPhotoEquipements().clear();
            for (PhotoEquipementDTO photoDTO : dto.getPhotoEquipements()) {
                PhotoEquipement photo = new PhotoEquipement();
                photo.setName(photoDTO.getName());
                photo.setEquipement(equipement);
                equipement.addPhoto(photo);
            }
        }
    }
}
