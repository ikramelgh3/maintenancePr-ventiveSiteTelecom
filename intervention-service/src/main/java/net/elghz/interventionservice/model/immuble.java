package net.elghz.interventionservice.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class immuble {
    private Long id;
    private String name;
    private String addr;
    private String codeImmuble;

    //site?
}
