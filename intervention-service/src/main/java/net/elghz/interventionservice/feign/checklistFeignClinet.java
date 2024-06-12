package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.PointMesureDTO;
import net.elghz.interventionservice.model.checklist;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(name = "CHECKLIST-SERVICE" , url = "http://localhost:8083/pointMesure")
public interface checklistFeignClinet {

    @GetMapping("/checklists/equip/{id}")
    List<PointMesureDTO> getChecklistsByEqui(@PathVariable Long id);

    @PostMapping("/add/checklist")
    public checklist addCHecklist(@RequestBody checklist dt);
}
