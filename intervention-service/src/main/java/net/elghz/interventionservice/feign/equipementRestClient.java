package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.Equipement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SITE-SERVICE" , url = "http://localhost:8082")
public interface equipementRestClient {
    @GetMapping("/equipement/{id}")
    public Equipement getEquiById(@PathVariable Long id);
}
