package net.elghz.siteservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "dr", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class DR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany (mappedBy = "dr" , cascade = CascadeType.ALL)
    List<DC> dcList = new ArrayList<>();
}
