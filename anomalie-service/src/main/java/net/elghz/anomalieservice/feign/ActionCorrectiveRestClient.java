package net.elghz.anomalieservice.feign;

import net.elghz.anomalieservice.model.ActionCorrective;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ACTIONCORRECTIVE-SERVICE" , url = "http://localhost:8089")
public interface ActionCorrectiveRestClient {
        @GetMapping("/actionsCorrectives/Anomalie")
        public List<ActionCorrective> actionsCorrectivesOfAnomalie(@RequestParam Long id);

        @DeleteMapping("/delete/ActionsCorrectives")
        public void deleteActionsCorrective(@RequestParam List<Long> idAcs );


}
