package com.demoV1Project.domain.dto.EmployeeWorkScheduleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeWorkScheduleRequest {
    private DayOfWeek dayOfWeek;
    private String openingMorningTime;  // Formato HH:mm
    private String closingMorningTime;
    private String openingEveningTime;
    private String closingEveningTime;
}
