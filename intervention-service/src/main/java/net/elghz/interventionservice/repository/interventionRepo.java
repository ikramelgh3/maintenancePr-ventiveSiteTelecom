package net.elghz.interventionservice.repository;

import net.elghz.interventionservice.dto.InterventionDTO;
import net.elghz.interventionservice.entities.Intervention;
import net.elghz.interventionservice.enumeration.statusIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface interventionRepo extends JpaRepository<Intervention , Long> {
    Optional<Intervention> findByName(String name);



    List<Intervention> findByStatus(statusIntervention status);

    @Query("SELECT c FROM Intervention c WHERE c.id_Planning = :idEq")
    List<Intervention> findById_Planning(Long idEq);

    @Query("SELECT c FROM Intervention c WHERE c.id_Techn = :idEqui")
    List<Intervention> findById_Equipe(Long idEqui);

    @Query("SELECT c FROM Intervention c WHERE c.id_Equipement = :idEqui")
    List<Intervention> findById_Equipement(Long idEqui);
}
