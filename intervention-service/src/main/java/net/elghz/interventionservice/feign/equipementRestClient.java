package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.Equipement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SITE-SERVICE" , url = "http://localhost:8082/sites")
public interface equipementRestClient {
    @GetMapping("/equipement/{id}")
    public Equipement getEquiById(@PathVariable Long id);

    @GetMapping("/getLocalisationOfEqui/{id}")
    public String getLocalisationOfEquip(@PathVariable Long id);

    @GetMapping("/getVille/{id}")
    public String getVilleEqui(@PathVariable Long id);
}
