package net.elghz.interventionservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class checklist {

    private Set<PointMesureDTO> measurementPoints = new HashSet<>();

    private Long id;

}
