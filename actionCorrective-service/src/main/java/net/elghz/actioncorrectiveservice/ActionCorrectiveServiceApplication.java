package net.elghz.actioncorrectiveservice;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ActionCorrectiveServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActionCorrectiveServiceApplication.class, args);
    }

}
