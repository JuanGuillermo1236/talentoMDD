package com.talentomdd.service;

import com.talentomdd.dto.AuthRegisterRequest;
import com.talentomdd.entity.Role;
import com.talentomdd.entity.User;
import com.talentomdd.exception.DuplicateResourceException;
import com.talentomdd.repository.CompanyRepository;
import com.talentomdd.repository.StudentRepository;
import com.talentomdd.repository.UserRepository;
import com.talentomdd.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de AuthService.
 * Ejecutar con: mvn test
 */
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, studentRepository, companyRepository,
                passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void registrarConEmailDuplicado_lanzaExcepcion() {
        AuthRegisterRequest request = new AuthRegisterRequest();
        request.setEmail("duplicado@unamad.edu.pe");
        request.setPassword("password123");
        request.setRole(Role.STUDENT);
        request.setFullName("Juan Perez");

        when(userRepository.existsByEmail("duplicado@unamad.edu.pe")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registrarComoAdmin_noPermitido() {
        AuthRegisterRequest request = new AuthRegisterRequest();
        request.setEmail("admin@unamad.edu.pe");
        request.setPassword("password123");
        request.setRole(Role.ADMIN);
        request.setFullName("Admin");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
