package com.talentomdd.service;

import com.talentomdd.dto.AuthLoginRequest;
import com.talentomdd.dto.AuthRegisterRequest;
import com.talentomdd.dto.AuthResponse;
import com.talentomdd.entity.Company;
import com.talentomdd.entity.Role;
import com.talentomdd.entity.Student;
import com.talentomdd.entity.User;
import com.talentomdd.exception.DuplicateResourceException;
import com.talentomdd.repository.CompanyRepository;
import com.talentomdd.repository.StudentRepository;
import com.talentomdd.repository.UserRepository;
import com.talentomdd.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(AuthRegisterRequest request) {
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("El rol ADMIN no puede autoregistrarse");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe una cuenta con ese correo");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();
        user = userRepository.save(user);

        if (request.getRole() == Role.STUDENT) {
            Student student = Student.builder()
                    .user(user)
                    .fullName(request.getFullName())
                    .build();
            studentRepository.save(student);
        } else if (request.getRole() == Role.COMPANY) {
            Company company = Company.builder()
                    .user(user)
                    .name(request.getFullName())
                    .build();
            companyRepository.save(company);
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .user(AuthResponse.UserSummary.builder()
                        .id(user.getId())
                        .role(user.getRole().name())
                        .build())
                .build();
    }

    public AuthResponse login(AuthLoginRequest request) {
        // BadCredentialsException es capturada por GlobalExceptionHandler -> 401
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado pero no encontrado"));

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .user(AuthResponse.UserSummary.builder()
                        .id(user.getId())
                        .role(user.getRole().name())
                        .build())
                .build();
    }
}
