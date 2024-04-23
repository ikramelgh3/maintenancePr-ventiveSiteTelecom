package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.Anomalie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ANOMALIE-SERVICE" , url = "http://localhost:8087")
public interface AnomalieRestClient {

    @GetMapping("/anomiles/DetectedIn/Interevntion")
    public List<Anomalie> anomaliesDetectedInIntervention(@RequestParam Long IdIntervention);
}
