package net.elghz.gestionparam.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_parametrage")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class parametrage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "type_parametrage", nullable = false)
        private String typeParametrage;

        @Column(name = "code_parametrage", nullable = false)
        private String codeParametrage;

        @Column(name = "libelle_param", nullable = false)
        private String libelleParam;

        @Column(name = "code_couleur")
        private String codeCouleur;

        @Column(name = "ordre_parametrage")
        private Integer ordreParametrage;

        @Column(name = "attr_1")
        private String attribut1;

        @Column(name = "attr_2")
        private String attribut2;

        @Column(name = "commentaire")
        private String commentaire;

        @Column(name = "flag_actif", nullable = false)
        private String flagActif;


    }
