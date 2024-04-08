package net.elghz.gestionparam.repository;

import net.elghz.gestionparam.entities.parametrage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface parametrageRepository  extends JpaRepository<parametrage, Long> {


    List<parametrage> findAllByTypeParametrage(String name);
   Optional<parametrage>  findByCodeParametrage(String name);

}
