package net.elghz.siteservice.repository;

import net.elghz.siteservice.entities.CentreTechnique;
import net.elghz.siteservice.entities.PhotoEquipement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoEquiRepo extends JpaRepository<PhotoEquipement, Long> {
}
