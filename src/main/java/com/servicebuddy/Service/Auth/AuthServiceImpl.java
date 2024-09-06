package com.servicebuddy.Service.Auth;

import com.servicebuddy.Config.JwtUtils;
import com.servicebuddy.DTO.LoginDto;
import com.servicebuddy.DTO.SignupDto;
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
    public UserDto registerClient(SignupDto signupDto) {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exists!");
        }

        Users user = modelMapper.map(signupDto, Users.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRole(UserRoles.CLIENT);

        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto registerServiceProvider(SignupDto signupDto) {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exists!");
        }

        Users user = modelMapper.map(signupDto, Users.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRole(UserRoles.SERVICE_PROVIDER);

        Users savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String authenticateUser(LoginDto loginDto) {
        Users user = userRepository.findByEmailOrPhone(loginDto.getEmailOrPhone(),loginDto.getEmailOrPhone())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email or phone: " + loginDto.getEmailOrPhone()));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getEmail());
        } else {
            throw new InvalidCredentialException("Invalid credentials");
        }
    }
}
