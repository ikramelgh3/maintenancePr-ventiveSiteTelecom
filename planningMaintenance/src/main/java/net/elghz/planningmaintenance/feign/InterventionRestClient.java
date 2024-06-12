package net.elghz.planningmaintenance.feign;

import jakarta.ws.rs.Path;
import net.elghz.planningmaintenance.model.Intervention;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "INTERVENTION-SERVICE" , url = "http://localhost:8086/interventions")
public interface InterventionRestClient {
    @GetMapping("/interventions/planning/{idPlanning}")
    List<Intervention> getInterventionsOfPlanning(@PathVariable Long idPlanning);

    @PostMapping("/add/Intervention/{idPlanning}")
    public Boolean addInterToPlanning(@PathVariable Long idPlanning,@RequestBody Intervention dt);

    @GetMapping("/find/Intervention/{id}")
    public Intervention findInterventionById(@PathVariable Long id);
}
