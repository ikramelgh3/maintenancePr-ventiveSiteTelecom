package net.elghz.planningmaintenance.model;

import lombok.Data;

@Data
public class ResponsableMaint {

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
