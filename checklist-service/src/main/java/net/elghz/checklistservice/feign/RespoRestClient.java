package net.elghz.checklistservice.feign;

import net.elghz.checklistservice.model.ResponsableMaintenance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE" ,url = "http://localhost:8084/users")
public interface RespoRestClient {
    @GetMapping("/respo/id/{id}")
    public ResponsableMaintenance findById(@PathVariable Long id);

}
