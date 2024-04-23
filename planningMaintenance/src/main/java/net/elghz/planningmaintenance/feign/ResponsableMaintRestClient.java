package net.elghz.planningmaintenance.feign;

import net.elghz.planningmaintenance.model.ResponsableMaint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USERS-SERVICE" , url = "http://localhost:8084")
public interface ResponsableMaintRestClient {

    @GetMapping("/respo/id/{id}")
    public ResponsableMaint findById(@PathVariable Long id);

}
