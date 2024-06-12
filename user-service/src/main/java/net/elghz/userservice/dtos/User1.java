package net.elghz.userservice.dtos;

import lombok.Data;

@Data
public class User1 {

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