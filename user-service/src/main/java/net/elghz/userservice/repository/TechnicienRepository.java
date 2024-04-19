package net.elghz.userservice.repository;

import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnicienRepository extends JpaRepository<Technicien, Long> {


    Technicien findByDisponibilite(Boolean disp);
    Technicien findByCompetences(List<Competence> competences);

    List<Technicien> findByCompetences_Competence(String competenceName);

    List<Technicien> findByDisponibiliteIsTrue();
}
