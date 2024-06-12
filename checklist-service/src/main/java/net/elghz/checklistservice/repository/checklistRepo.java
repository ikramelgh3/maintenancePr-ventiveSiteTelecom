package net.elghz.checklistservice.repository;

import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.model.typeEquipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface checklistRepo extends JpaRepository<Checklist , Long> {

}