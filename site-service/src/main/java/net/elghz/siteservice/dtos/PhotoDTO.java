package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.entities.Site;

import java.util.Date;

@Data
public class PhotoDTO {

    private Long id;
    private String name;

    private String type;
    private byte[] picByte;
    private Date dateAjout;
}
