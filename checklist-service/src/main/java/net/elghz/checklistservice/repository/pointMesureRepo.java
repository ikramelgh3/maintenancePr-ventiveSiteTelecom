package net.elghz.checklistservice.repository;

import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface pointMesureRepo extends JpaRepository<PointMesure, Long> {
}
