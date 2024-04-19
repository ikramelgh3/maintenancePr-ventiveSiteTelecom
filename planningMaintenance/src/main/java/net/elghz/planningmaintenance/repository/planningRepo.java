package net.elghz.planningmaintenance.repository;

import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface planningRepo extends JpaRepository<PlanningMaintenance, Long> {


    Optional<PlanningMaintenance> findByName(String name);

    List<PlanningMaintenance> findByStatus(PlanningStatus status);
}
