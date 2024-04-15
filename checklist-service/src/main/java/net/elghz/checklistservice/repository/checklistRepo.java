package net.elghz.checklistservice.repository;

import net.elghz.checklistservice.entities.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface checklistRepo  extends JpaRepository<Checklist , Long> {
}
