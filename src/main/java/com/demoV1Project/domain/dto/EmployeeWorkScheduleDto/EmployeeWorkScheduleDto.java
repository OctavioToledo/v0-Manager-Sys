package com.demoV1Project.domain.dto.EmployeeWorkScheduleDto;

import com.demoV1Project.util.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeWorkScheduleDto {
    private Long id;
    private DayOfWeek dayOfWeek;
    private String openingMorningTime;
    private String closingMorningTime;
    private String openingEveningTime;
    private String closingEveningTime;
}
