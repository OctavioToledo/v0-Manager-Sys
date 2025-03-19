package com.demoV1Project.domain.dto.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateDto {

    private String status;
    private Date date;
    private Long employeeId;
    private Long serviceId;


}
