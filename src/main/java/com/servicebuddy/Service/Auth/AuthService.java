package com.servicebuddy.Service.Auth;

import com.servicebuddy.DTO.LoginRequestDto;
import com.servicebuddy.DTO.SignupRequestDto;
import com.servicebuddy.DTO.UserDto;

public interface AuthService {

    UserDto registerClient(SignupRequestDto signupRequestDto);
    UserDto registerServiceProvider(SignupRequestDto signupRequestDto);
    public String authenticateUser(LoginRequestDto loginRequestDto);

}
