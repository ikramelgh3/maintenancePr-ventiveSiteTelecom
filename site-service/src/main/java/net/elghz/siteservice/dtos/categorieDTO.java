package net.elghz.siteservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import net.elghz.siteservice.entities.Attribute;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
public class categorieDTO {


    private Long id;

    private  String name;
   // @JsonManagedReference
 //   private List<attributeDTO> attributes= new ArrayList<>();
}
