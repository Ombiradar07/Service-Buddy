package com.servicebuddy.Controller;

import com.servicebuddy.DTO.LoginRequestDto;
import com.servicebuddy.DTO.SignupRequestDto;
import com.servicebuddy.DTO.UserDto;
import com.servicebuddy.Service.Auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/client/sign-up")
    public ResponseEntity<UserDto> signupClient(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        UserDto savedUser = authService.registerClient(signupRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<UserDto> signupServiceProvider(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        UserDto savedUser = authService.registerServiceProvider(signupRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequestDto loginDto) {
        String token = authService.authenticateUser(loginDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
