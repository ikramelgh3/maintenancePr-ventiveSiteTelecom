package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepo extends JpaRepository<categorie, Long> {
    Optional<categorie> findByName(String name);


    Optional<categorie> findByAttributesName(String attributeName);
}
