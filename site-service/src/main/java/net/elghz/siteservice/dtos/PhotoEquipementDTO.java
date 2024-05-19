package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
public class PhotoEquipementDTO {

    private Long id;
    private String name;

    private String type;
    private byte[] picByte;
    private Date dateAjout;

}
