package com.demoV1Project.application.exceptions;

public class InvalidEmployeeWorkScheduleException extends RuntimeException {
    public InvalidEmployeeWorkScheduleException(String message) {
        super(message);
    }
}

