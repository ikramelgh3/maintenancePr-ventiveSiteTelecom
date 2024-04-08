package net.elghz.siteservice.dtos;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CentreTechniqueDTO {
    private Long id ;
    private String name;
    private DCDTO dc;
    //List<siteDTO> sites = new ArrayList<>();
}
