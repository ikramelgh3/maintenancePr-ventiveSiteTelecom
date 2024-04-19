package net.elghz.userservice.feign;


import net.elghz.userservice.model.ChecklistDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CHECKLIST-SERVICE" , url="http://localhost:8083")
public interface checklistRestClient {
    @GetMapping("/checklists/respo/{idR}")
    List<ChecklistDTO>  getChecklistsByResp(@PathVariable Long idR);

}
