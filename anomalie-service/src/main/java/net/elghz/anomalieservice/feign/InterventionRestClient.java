package net.elghz.anomalieservice.feign;

import net.elghz.anomalieservice.model.Interevntion;
import org.hibernate.Internal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INTERVENTION-SERVICE" , url = "http://localhost:8086")
public interface InterventionRestClient {

    @GetMapping("/find/Intervention")
    public Interevntion findInterventionById(@RequestParam Long id);
}
