package com.demoV1Project.domain.dto.AppointmentDto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AppointmentCreateDto {
    private Long id;
    private String status;
    private Date date;
    private Long employeeId;
    private Long serviceId;
    private Long userId;
}
