package net.elghz.siteservice.dtos;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
public class DCDTO {


    private Long id ;
    private String name;

    //private List<CentreTechniqueDTO> centreTechniqueList = new ArrayList<>();

    private DRDTO dr;
}
