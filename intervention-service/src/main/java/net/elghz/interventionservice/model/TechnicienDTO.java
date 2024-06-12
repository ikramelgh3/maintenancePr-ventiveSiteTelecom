package net.elghz.interventionservice.model;

import lombok.Data;
import net.elghz.interventionservice.enumeration.TypeTechnicien;

import java.util.HashSet;
import java.util.Set;
@Data
public class TechnicienDTO {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String password;
    // private boolean enabled;
    private String phone_number;
    private String jobTitle;
    private String available;
    private String city;
    //private String[] skills;
    private String type;
    private String roleName;

}
