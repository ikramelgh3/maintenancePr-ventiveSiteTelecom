package net.elghz.siteservice.mapper;

import net.elghz.siteservice.dtos.*;
import net.elghz.siteservice.entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class siteMobileMapper {

    @Autowired
    private equipementMapper equipementMapper;
    @Autowired
    private typeActiviteMapper typeActiviteMapper;
    private final ModelMapper modelMapper = new ModelMapper();


    public void update(SiteMobileDTO dto, SiteMobile site) {
        site.setName(dto.getName());
        site.setAddresse(dto.getAddresse());
        site.setCode(dto.getCode());
        // site.setCentreTechnique(dto.getCentreTechnique());
        site.setLatitude(dto.getLatitude());
        site.setLongitude(dto.getLongitude());
        site.setPresenceGESecours(dto.getPresenceGESecours());
        site.setTypeInstallation(dto.getTypeInstallation());
        site.setTypeAlimentation(dto.getTypeAlimentation());
        site.setTypeTransmission(dto.getTypeTransmission());
        site.setLieuInsatallationBTS(dto.getLieuInsatallationBTS());
        site.setHauteurSupportAntenne(dto.getHauteurSupportAntenne());
        site.setSupportAntennes(dto.getSupportAntennes());

        if (dto.getTypeactivites() != null) {
            Set<TypeActivite> typeActivites = new HashSet<>();
            for (typeActiviteDTO typeActiviteDTO : dto.getTypeactivites()) {
                typeActivites.add(typeActiviteMapper.from(typeActiviteDTO));
            }
            site.setTypeactivites(typeActivites);
        }

//        if (dto.getPhotos() != null) {
//            List<Photo> photos = new ArrayList<>();
//            for (PhotoDTO photoDTO : dto.getPhotos()) {
//                photos.add(modelMapper.map(photoDTO, Photo.class));
//            }
//            site.setPhotos(photos);
//        }
    }
}