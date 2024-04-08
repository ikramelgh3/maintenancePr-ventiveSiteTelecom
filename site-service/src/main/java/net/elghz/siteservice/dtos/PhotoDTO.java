package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.entities.Site;
@Data
public class PhotoDTO {

    private Long id;
    private String name;

}
