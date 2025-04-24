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
public class EmployeeScheduleGridDto {
    private Long employeeId;
    private String employeeName; // o nombre + apellido
    private List<TimeSlotDto> timeSlots;
}
