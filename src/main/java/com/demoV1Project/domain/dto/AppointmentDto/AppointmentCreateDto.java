package com.demoV1Project.domain.dto.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateDto {
    private Long id;
    private String status;
    private LocalDateTime date;
    private Long businessId;
    private Long employeeId;
    private Long serviceId;
    private Long userId;
}
