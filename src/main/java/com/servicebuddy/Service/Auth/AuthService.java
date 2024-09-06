package com.servicebuddy.Service.Auth;

import com.servicebuddy.DTO.LoginDto;
import com.servicebuddy.DTO.SignupDto;
import com.servicebuddy.DTO.UserDto;

public interface AuthService {

    UserDto registerClient(SignupDto signupDto);
    UserDto registerServiceProvider(SignupDto signupDto);
    public String authenticateUser(LoginDto loginDto);

}
