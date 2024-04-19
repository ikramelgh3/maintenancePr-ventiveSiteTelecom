package net.elghz.checklistservice.repository;

import net.elghz.checklistservice.entities.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface checklistRepo  extends JpaRepository<Checklist , Long> {
    @Query("SELECT c FROM Checklist c WHERE c.respo_Id = :respoId")
    List<Checklist> findByRespoId(Long respoId);
    @Query("SELECT c FROM Checklist c WHERE c.equipement_id = :idEq")
    List<Checklist> findByEqui_Id(Long idEq);
}
