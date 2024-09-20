package com.servicebuddy.Service.Auth;

import com.servicebuddy.Config.JwtUtils;
import com.servicebuddy.DTO.LoginRequestDto;
import com.servicebuddy.DTO.SignupRequestDto;
import com.servicebuddy.DTO.UserDto;
import com.servicebuddy.Entity.Users;
import com.servicebuddy.Enum.UserRoles;
import com.servicebuddy.Exception.InvalidCredentialException;
import com.servicebuddy.Exception.ResourceNotFoundException;
import com.servicebuddy.Exception.UserAlreadyExistException;
import com.servicebuddy.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDto registerClient(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exists!");
        }

        Users user = modelMapper.map(signupRequestDto, Users.class);
        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setRole(UserRoles.CLIENT);

        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto registerServiceProvider(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exists!");
        }

        Users user = modelMapper.map(signupRequestDto, Users.class);
        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setRole(UserRoles.SERVICE_PROVIDER);

        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String authenticateUser(LoginRequestDto loginRequestDto) {
        Users user = userRepository.findByEmailOrPhone(loginRequestDto.getEmailOrPhone(), loginRequestDto.getEmailOrPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email or phone: " + loginRequestDto.getEmailOrPhone()));

        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getEmail());
        } else {
            throw new InvalidCredentialException("Invalid credentials");
        }
    }
}
