package com.servicebuddy.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String emailOrPhone;
    private String password;
}
