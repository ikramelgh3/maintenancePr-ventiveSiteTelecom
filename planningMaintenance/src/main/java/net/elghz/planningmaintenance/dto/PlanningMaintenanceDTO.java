package net.elghz.planningmaintenance.dto;

import jakarta.persistence.*;
import lombok.*;
import net.elghz.planningmaintenance.enumeration.PlanningStatus;
import net.elghz.planningmaintenance.model.Intervention;
import net.elghz.planningmaintenance.model.ResponsableMaint;
import net.elghz.planningmaintenance.model.Site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PlanningMaintenanceDTO {
    private Long id;
    private String name;
    private Date dateDebutRealisation;
    private Date dateFinRealisation;
    private String semestre;
    private PlanningStatus status;
    private Long id_Site;
    private Site site;
    private String description;
    private Date dateCreation;
    private Long id_Respo;
    private ResponsableMaint responsableMaint;
    List<Intervention> interventionList = new ArrayList<>();

}
