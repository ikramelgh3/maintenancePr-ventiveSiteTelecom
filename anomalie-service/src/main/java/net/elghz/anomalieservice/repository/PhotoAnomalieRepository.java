package net.elghz.anomalieservice.repository;

import net.elghz.anomalieservice.entities.PhotoAnomalie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoAnomalieRepository extends JpaRepository<PhotoAnomalie, Long> {
}
