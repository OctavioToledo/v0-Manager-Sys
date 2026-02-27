package com.demoV1Project.application.exceptions;

public class BusinessAccessDeniedException extends RuntimeException {
    public BusinessAccessDeniedException(String message) {
        super(message);
    }

    public BusinessAccessDeniedException(Long businessId) {
        super("No tienes permiso para acceder al negocio con id: " + businessId);
    }
}
