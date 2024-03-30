package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.siteservice.enumeration.AttributeCategory;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
@Entity
public class Attribute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String attributeValue;
    @Enumerated(EnumType.STRING)
    private AttributeCategory attributeCategory;

    public Attribute(String dr, String dra, AttributeCategory attributeCategory) {
        this.name = name;
        this.attributeValue = attributeValue;
        this.attributeCategory = attributeCategory;
    }
}
