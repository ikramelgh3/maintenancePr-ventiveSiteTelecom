package net.elghz.actioncorrectiveservice.repository;

import net.elghz.actioncorrectiveservice.entities.ActionCorrective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionCorrectiveRepository extends JpaRepository<ActionCorrective , Long> {


    @Query("SELECT c FROM ActionCorrective c WHERE c.idAnomalie = :idAN")
    List<ActionCorrective> findByAnomalie_Id(Long idAN);
}
