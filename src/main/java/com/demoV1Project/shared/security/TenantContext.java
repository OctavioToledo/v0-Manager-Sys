package com.demoV1Project.shared.security;

import com.demoV1Project.application.exceptions.BusinessAccessDeniedException;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Resuelve el tenant (usuario + negocio) a partir del JWT en el
 * SecurityContext.
 * Soporta tanto JWTs de Auth0 (sub = "auth0|xxx") como locales (sub = userId
 * numérico).
 */
@Component
@RequiredArgsConstructor
public class TenantContext {

    private final UserRepository userRepository;

    /**
     * Obtiene el User actual a partir del JWT sub claim.
     * Si el sub es numérico (JWT local), busca por ID.
     * Si el sub contiene "auth0|", busca por auth0Sub.
     */
    public User getCurrentUser() {
        String sub = extractSub();

        // Local JWT: sub es el ID numérico del usuario
        try {
            Long userId = Long.parseLong(sub);
            return userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException(
                            "Usuario no encontrado para id: " + userId));
        } catch (NumberFormatException e) {
            // Auth0 JWT: sub es "auth0|xxx" o "google-oauth2|xxx"
            return userRepository.findByAuth0Sub(sub)
                    .orElseThrow(() -> new IllegalStateException(
                            "Usuario no encontrado para auth0Sub: " + sub));
        }
    }

    /**
     * Valida que el usuario autenticado sea dueño del negocio indicado.
     */
    public void validateBusinessOwnership(Long businessId) {
        User user = getCurrentUser();
        boolean isOwner = user.getBusinessList().stream()
                .anyMatch(b -> b.getId().equals(businessId));

        if (!isOwner) {
            throw new BusinessAccessDeniedException(businessId);
        }
    }

    /**
     * Obtiene el Business del usuario actual validando ownership.
     */
    public Business getOwnedBusiness(Long businessId) {
        User user = getCurrentUser();
        return user.getBusinessList().stream()
                .filter(b -> b.getId().equals(businessId))
                .findFirst()
                .orElseThrow(() -> new BusinessAccessDeniedException(businessId));
    }

    private String extractSub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("No se encontró un JWT válido en el contexto de seguridad");
        }
        return jwt.getSubject();
    }
}
