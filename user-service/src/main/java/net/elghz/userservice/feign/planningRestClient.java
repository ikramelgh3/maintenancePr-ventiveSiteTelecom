package net.elghz.userservice.feign;

import net.elghz.userservice.model.Planning;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PLANNING-SERVICE" , url = "http://localhost:8085/planningsMaintenances")
public interface planningRestClient {
        @GetMapping("/get/planningOfRespo/{idResp}")
        public List<Planning> planningOfRespo(@PathVariable Long idResp);

}
