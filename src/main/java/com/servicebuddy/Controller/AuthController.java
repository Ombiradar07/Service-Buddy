package com.servicebuddy.Controller;

import com.servicebuddy.DTO.LoginDto;
import com.servicebuddy.DTO.SignupDto;
import com.servicebuddy.DTO.UserDto;
import com.servicebuddy.Service.Auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/client/sign-up")
    public ResponseEntity<UserDto> signupClient(@Valid @RequestBody SignupDto signupDto) {
        UserDto savedUser = authService.registerClient(signupDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/company/sign-up")
    public ResponseEntity<UserDto> signupServiceProvider(@Valid @RequestBody SignupDto signupDto) {
        UserDto savedUser = authService.registerServiceProvider(signupDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        String token = authService.authenticateUser(loginDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
