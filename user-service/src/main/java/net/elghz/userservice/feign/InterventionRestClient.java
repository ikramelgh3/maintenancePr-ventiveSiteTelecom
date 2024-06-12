package net.elghz.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INTERVENTION-SERVICE" , url = "http://localhost:8086/interventions")
public interface InterventionRestClient {


    @GetMapping("/all/Interventions/Equipe/{id}")
    public ResponseEntity<?> allInterventionsOfEquipe(@PathVariable Long id);
}
