package com.demoV1Project.domain.dto.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;
    private String status;
    private LocalDate appointmentDate;
    private String startTime;
    private String endTime;
    private Long businessId;
    private Long employeeId;
    private Long serviceId;
    private Long userId;
}
