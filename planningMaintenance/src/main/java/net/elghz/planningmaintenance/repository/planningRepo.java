package net.elghz.planningmaintenance.repository;

import net.elghz.planningmaintenance.entities.PlanningMaintenance;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface planningRepo extends PagingAndSortingRepository<PlanningMaintenance, Long> , JpaRepository<PlanningMaintenance, Long> {


    Optional<PlanningMaintenance> findByName(String name);

    List<PlanningMaintenance> findByStatus(PlanningStatus status);

    List<PlanningMaintenance> findBySemestre(String semestre);
    @Query("SELECT c FROM PlanningMaintenance c WHERE c.id_Site = :idSite")
    List<PlanningMaintenance> findById_Site(Long idSite);

    @Query("SELECT c FROM PlanningMaintenance c WHERE c.id_Respo = :idRespo")
    List<PlanningMaintenance> findById_Respo(String idRespo);

    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);
    @Query("SELECT c FROM PlanningMaintenance  c where  c.name like %?1% OR c.semestre like %?1%")
    public List<PlanningMaintenance> findAllPln(String keyword );
}
