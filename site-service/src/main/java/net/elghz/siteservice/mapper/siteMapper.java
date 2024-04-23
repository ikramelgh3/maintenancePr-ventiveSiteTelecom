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

    public SiteMobileDTO fromMobile (SiteMobile e){
        return modelMapper.map(e, SiteMobileDTO.class);
    }

    public SiteMobile fromMobileDTO (SiteMobileDTO   siteDTO){
        return modelMapper.map(siteDTO,SiteMobile.class);
    }

    public SiteFixeDTO fromFixe (SiteFixe e){
        return modelMapper.map(e, SiteFixeDTO.class);
    }

    public SiteFixe fromFixeDTO (SiteFixeDTO   siteDTO){
        return modelMapper.map(siteDTO,SiteFixe.class);
    }

    public siteDTO mapToSiteDTO(SiteFixeDTO siteFixeDTO) {
        return modelMapper.map(siteFixeDTO, siteDTO.class);
    }

    public siteDTO mapToSiteDTO(SiteMobileDTO siteMobileDTO) {
        return modelMapper.map(siteMobileDTO, siteDTO.class);
    }

    public salleDTO from (salle e){
        return modelMapper.map(e, salleDTO.class);
    }

    public salle from (salleDTO   siteDTO){
        return modelMapper.map(siteDTO,salle.class);
    }


    public immubleDTO from (immuble e){
        return modelMapper.map(e, immubleDTO.class);
    }

    public immuble from (immubleDTO   siteDTO){
        return modelMapper.map(siteDTO,immuble.class);
    }

    public etageDTO from (etage e){
        return modelMapper.map(e, etageDTO.class);
    }

    public etage from (etageDTO   siteDTO){
        return modelMapper.map(siteDTO,etage.class);
    }


    public void update(siteDTO dto, Site site) {
        site.setName(dto.getName());
        site.setAddresse(dto.getAddresse());
        site.setCode(dto.getCode());
       // site.setCentreTechnique(dto.getCentreTechnique());
        site.setLatitude(dto.getLatitude());
        site.setLongitude(dto.getLongitude());
        site.setPresenceGESecours(dto.getPresenceGESecours());
        site.setTypeInstallation(dto.getTypeInstallation());
        site.setTypeAlimentation(dto.getTypeAlimentation());

        //site.setType(dto.getType());
/*
        if (dto.getAttributs() != null) {
            Set<Attribute> attributs = new HashSet<>();
            for (attributeDTO attributDTO : dto.getAttributs()) {
                attributs.add(modelMapper.map(attributDTO, Attribute.class));
            }
            site.setAttributs(attributs);
        }
*/


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

