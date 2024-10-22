package net.elghz.planningmaintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@EnableFeignClients
@SpringBootApplication

public class PlanningMaintenanceApplication {


    public static void main(String[] args) {
        SpringApplication.run(PlanningMaintenanceApplication.class, args);
    }


}
