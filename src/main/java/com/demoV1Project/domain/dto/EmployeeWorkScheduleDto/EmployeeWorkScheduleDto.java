package com.demoV1Project.domain.dto.EmployeeWorkScheduleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeWorkScheduleDto {
    private Long id;
    private Integer dayOfWeek;
    private Boolean isWorkingDay;
    private LocalTime morningStart;
    private LocalTime morningEnd;
    private LocalTime afternoonStart;
    private LocalTime afternoonEnd;
}
