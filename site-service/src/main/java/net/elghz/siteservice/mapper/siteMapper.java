package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class siteMapper {
    @Autowired  private equipementMapper equipementMapper;
  @Autowired
  private typeActiviteMapper typeActiviteMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    public siteDTO from (Site e){
        return modelMapper.map(e, siteDTO.class);
    }

    public Site from (siteDTO   siteDTO){
        return modelMapper.map(siteDTO,Site.class);
    }
    public void update(siteDTO dto, Site site) {
        site.setName(dto.getName());
        site.setType(dto.getType());

        if (dto.getAttributs() != null) {
            Set<Attribute> attributs = new HashSet<>();
            for (attributeDTO attributDTO : dto.getAttributs()) {
                attributs.add(modelMapper.map(attributDTO, Attribute.class));
            }
            site.setAttributs(attributs);
        }

        if (dto.getEquipements() != null) {
            List<equipement> equipements = new ArrayList<>();
            for (equipementDTO equipementDTO : dto.getEquipements()) {
                equipements.add(equipementMapper.from(equipementDTO));
            }
            site.setEquipements(equipements);
        }

        if (dto.getTypeactivites() != null) {
            Set<TypeActivite> typeActivites = new HashSet<>();
            for (typeActiviteDTO typeActiviteDTO : dto.getTypeactivites()) {
                typeActivites.add(typeActiviteMapper.from(typeActiviteDTO));
            }
            site.setTypeactivites(typeActivites);
        }

        if (dto.getPhotos() != null) {
            List<Photo> photos = new ArrayList<>();
            for (PhotoDTO photoDTO : dto.getPhotos()) {
                photos.add(modelMapper.map(photoDTO, Photo.class));
            }
            site.setPhotos(photos);
        }
    }

}

