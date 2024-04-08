package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class DRDTO {

    private Long id ;
    private String name;

    //List<DCDTO> dcList = new ArrayList<>();
}
