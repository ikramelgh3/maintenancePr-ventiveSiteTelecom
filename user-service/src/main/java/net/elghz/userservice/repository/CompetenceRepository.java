package net.elghz.userservice.repository;

import net.elghz.userservice.entities.Competence;
import net.elghz.userservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence, Long> {

    Competence findByCompetence(String name);

    List<Competence> findByCompetenceIn(List<String> competenceNames);
}
