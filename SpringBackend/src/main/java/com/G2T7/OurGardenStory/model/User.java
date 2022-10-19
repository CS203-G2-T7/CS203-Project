package com.G2T7.OurGardenStory.model;

import java.time.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String address;
    private String phoneNumber;
    private String DOB;
    private String password;
    private LocalDate DateCreated;
    private String given_name;
}
