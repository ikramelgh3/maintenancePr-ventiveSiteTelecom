package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.typeEquipement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface typeEquipementRepo extends JpaRepository<typeEquipement, Long> {

    Optional<typeEquipement> findByName(String name);
}
