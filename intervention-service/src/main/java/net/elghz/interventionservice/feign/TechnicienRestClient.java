package net.elghz.interventionservice.feign;

import net.elghz.interventionservice.model.Equipe;
import net.elghz.interventionservice.model.TechnicienDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "USERS-SERVICE" , url = "http://localhost:8084")
public interface TechnicienRestClient {


    @GetMapping("/technicien/{id}")
    public TechnicienDTO findTechnicienById(@PathVariable Long id);

    @GetMapping("/keycloak/users/{id}")
    public TechnicienDTO getUser(@PathVariable("id") String id);
    @GetMapping("/keycloak/users/technicians")
    public List<TechnicienDTO> getTechnicians();
    @GetMapping("/techniciens/filter-by-city/{city}")
    public List<TechnicienDTO> filterTechnicienByCity(@PathVariable("city") String city);
}
