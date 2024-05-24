package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] picByte;
    private Date dateAjout;
    @ManyToOne @JoinColumn(name = "site_id")
    private Site site;

    public Photo(String imageName, Site site) {
        this.name=name;
        this.site= site;
    }


    public Photo(String originalFilename, String contentType, byte[] bytes) {
    }
}
