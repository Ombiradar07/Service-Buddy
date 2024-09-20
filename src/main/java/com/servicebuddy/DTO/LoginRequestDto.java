package com.servicebuddy.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Email/phone is required")
    private String emailOrPhone;

    @NotBlank(message = "Password is required")
    private String password;
}
