package net.elghz.planningmaintenance.feign;

import net.elghz.planningmaintenance.model.Site;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SITE-SERVICE" , url = "http://localhost:8082")
public interface SiteRestClient {

    @GetMapping("/site/id/{id}")
    public Site findType(@PathVariable Long id);


}