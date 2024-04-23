package net.elghz.anomalieservice.repository;

import net.elghz.anomalieservice.entities.Anomalie;
import net.elghz.anomalieservice.enumeration.StatusGravite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface anomalieRepository extends JpaRepository<Anomalie,Long> {
    List<Anomalie> findByStatus(StatusGravite statusGravite);

    @Query("SELECT c FROM Anomalie c WHERE c.id_TechnicienD = :idTech")
    List<Anomalie> getAnomaliesByIdTech(Long idTech);

    @Query("SELECT c FROM Anomalie c WHERE c.id_Intervention = :idInter")
    List<Anomalie> getAnomalieDetectedInIntervention(Long idInter);


}
