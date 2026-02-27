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
 * Uso: inyectar en controllers o services para validar ownership.
 */
@Component
@RequiredArgsConstructor
public class TenantContext {

    private final UserRepository userRepository;

    /**
     * Obtiene el User actual a partir del JWT sub claim.
     * 
     * @throws IllegalStateException si no hay JWT o el usuario no existe
     */
    public User getCurrentUser() {
        String auth0Sub = extractAuth0Sub();
        return userRepository.findByAuth0Sub(auth0Sub)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuario no encontrado para auth0Sub: " + auth0Sub));
    }

    /**
     * Valida que el usuario autenticado sea dueño del negocio indicado.
     * 
     * @throws BusinessAccessDeniedException si el user no es dueño
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
     * Útil como shortcut cuando solo se necesita el business.
     */
    public Business getOwnedBusiness(Long businessId) {
        User user = getCurrentUser();
        return user.getBusinessList().stream()
                .filter(b -> b.getId().equals(businessId))
                .findFirst()
                .orElseThrow(() -> new BusinessAccessDeniedException(businessId));
    }

    private String extractAuth0Sub() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("No se encontró un JWT válido en el contexto de seguridad");
        }
        return jwt.getSubject();
    }
}
