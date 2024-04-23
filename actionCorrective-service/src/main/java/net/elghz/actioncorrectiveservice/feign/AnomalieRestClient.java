package net.elghz.actioncorrectiveservice.feign;

import net.elghz.actioncorrectiveservice.model.Anomalie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ANOMALIE-SERVICE" , url = "http://localhost:8087")
public interface AnomalieRestClient {

    @GetMapping("/get/anomalie/{id}")
    public Anomalie getAnomalieById(@PathVariable Long id);
}
