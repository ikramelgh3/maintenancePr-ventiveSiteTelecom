package net.elghz.checklistservice.feign;

import net.elghz.checklistservice.model.ResponsableMaintenance;
import net.elghz.checklistservice.model.equipement;
import net.elghz.checklistservice.model.typeEquipement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "SITE-SERVICE" , url = "http://localhost:8082")
public interface EquipementRestClient {

    @GetMapping("/findTypeEqui/{id}")
    public typeEquipement findTypeEquipemntById(@PathVariable Long id);

    @GetMapping("/find/byType/equipement/{name}")
    public Optional<typeEquipement> findByName(@PathVariable("name") String name);

    @PostMapping("/add/type/equipement")
    public typeEquipement addTypeEquipement(@RequestBody typeEquipement type);

}
