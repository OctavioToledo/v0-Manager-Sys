package com.demoV1Project.domain.dto.BusinessHours;

import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {

        private DayOfWeek dayOfWeek;
        private LocalTime openingMorningTime;
        private LocalTime closingMorningTime;
        private LocalTime openingEveningTime;
        private LocalTime closingEveningTime;
}
