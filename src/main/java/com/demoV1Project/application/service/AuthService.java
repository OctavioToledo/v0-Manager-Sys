package com.demoV1Project.application.service;

import com.demoV1Project.config.security.JwtTokenProvider;
import com.demoV1Project.domain.dto.AuthDto.AuthResponse;
import com.demoV1Project.domain.dto.AuthDto.SignInRequest;
import com.demoV1Project.domain.dto.AuthDto.SignUpRequest;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import com.demoV1Project.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Autentica un usuario por email/password y devuelve un JWT local.
     */
    public AuthResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos"));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        Long businessId = (user.getBusinessList() != null && !user.getBusinessList().isEmpty())
                ? user.getBusinessList().get(0).getId()
                : null;

        String token = jwtTokenProvider.generateToken(
                user.getId(), user.getEmail(), user.getRole().name(), businessId);

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .build();
    }

    /**
     * Registra un nuevo usuario, hashea su password y devuelve un JWT local.
     */
    public AuthResponse signUp(SignUpRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(
                user.getId(), user.getEmail(), user.getRole().name(), null);

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .build();
    }
}
