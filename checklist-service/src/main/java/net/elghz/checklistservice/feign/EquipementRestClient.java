package net.elghz.checklistservice.feign;

import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SITE-SERVICE" , url = "http://localhost:8082")
public interface EquipementRestClient {

    @GetMapping("/equip/id/{id}")
    public equipement findEquipById(@PathVariable Long id);

}
