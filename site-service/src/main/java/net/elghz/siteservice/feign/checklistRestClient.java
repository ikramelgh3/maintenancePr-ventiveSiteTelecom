package net.elghz.siteservice.feign;

import net.elghz.siteservice.model.ChecklistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CHECKLIST-SERVICE" , url="http://localhost:8083")
public interface checklistRestClient {
    @GetMapping("/checklist/equip/name/{name}")
    public ResponseEntity<?> getChecklistsEquipelt(@PathVariable String name);
}