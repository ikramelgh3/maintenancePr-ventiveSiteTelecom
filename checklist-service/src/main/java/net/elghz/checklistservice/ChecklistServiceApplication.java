package net.elghz.checklistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ChecklistServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecklistServiceApplication.class, args);
    }

}
