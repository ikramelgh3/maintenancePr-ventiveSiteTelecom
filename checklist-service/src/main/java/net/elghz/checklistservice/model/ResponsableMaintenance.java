package net.elghz.checklistservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;

import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter @Setter
public class ResponsableMaintenance {
    private String nom;

    private String username;

 //   private String password;



}
