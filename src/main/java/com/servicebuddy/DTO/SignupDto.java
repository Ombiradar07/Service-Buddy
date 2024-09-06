package com.servicebuddy.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;

}
