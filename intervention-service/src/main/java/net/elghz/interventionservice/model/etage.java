package net.elghz.interventionservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class etage {

    private  Long id;
    private int numeroEtage;
    private int nbreSalle;

    private String codeEtagge;

    private immuble immuble;
}
