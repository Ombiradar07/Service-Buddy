package com.servicebuddy.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "Email/phone is required")
    private String emailOrPhone;

    @NotBlank(message = "Password is required")
    private String password;
}
