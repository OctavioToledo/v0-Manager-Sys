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
public class EmployeeWorkScheduleRequest {
    private DayOfWeek dayOfWeek;
    private LocalTime openingMorningTime;
    private LocalTime closingMorningTime;
    private LocalTime openingEveningTime;
    private LocalTime closingEveningTime;
}
