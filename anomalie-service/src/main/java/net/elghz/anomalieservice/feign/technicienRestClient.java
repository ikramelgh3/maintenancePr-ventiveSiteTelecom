package net.elghz.anomalieservice.feign;

import net.elghz.anomalieservice.model.Technicien;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERS-SERVICE" , url = "http://localhost:8084")
public interface technicienRestClient {
    @GetMapping("/technicien/{id}")
    public Technicien findTechnicienById(@PathVariable Long id);
}
