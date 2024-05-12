package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.Planning;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient (name = "PLANNING-SERVICE" , url = "http://localhost:8085")
public interface PlannigRestClient {

    @GetMapping("/find/planning/{id}")
    public Planning findPlanningById(@PathVariable Long id);


}
