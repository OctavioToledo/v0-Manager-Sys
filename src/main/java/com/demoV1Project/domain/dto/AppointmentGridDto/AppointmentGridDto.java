package com.demoV1Project.domain.dto.AppointmentGridDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentGridDto {
    private String serviceName;
    private int serviceDuration;
    private List<TimeSlotDto> timeSlots;
    private List<EmployeeScheduleGridDto> employeeAvailabilities;
}
