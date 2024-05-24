package net.elghz.checklistservice.repository;

import net.elghz.checklistservice.dtos.PointMesureDTO;
import net.elghz.checklistservice.entities.Checklist;
import net.elghz.checklistservice.entities.PointMesure;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface pointMesureRepo extends JpaRepository<PointMesure, Long> {


   List<PointMesure> findByTypeEquipementId(Long id);

   PointMesure findByAttribut(String  att);

    PointMesure findByAttributAndTypeEquipementId(String description, Long id);

    Boolean existsByAttributAndIdIsNot(String name, Long id);
    Boolean existsByAttribut(String name);
}
