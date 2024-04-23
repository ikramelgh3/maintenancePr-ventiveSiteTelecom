package net.elghz.siteservice.feign;

import net.elghz.siteservice.model.Planning;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PLANNING-SERVICE" , url = "http://localhost:8085")
public interface PlanningRestClient {

    @GetMapping("/get/planningOfSite/{idSite}")
    public List<Planning> planningOfSite(@PathVariable Long idSite);
}
