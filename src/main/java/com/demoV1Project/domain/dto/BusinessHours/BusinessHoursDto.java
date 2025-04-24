package com.demoV1Project.domain.dto.BusinessHours;


import lombok.*;
import java.time.DayOfWeek;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {

        private DayOfWeek dayOfWeek;
        private String openingMorningTime;  // Formato HH:mm
        private String closingMorningTime;
        private String openingEveningTime;  // Nullable
        private String closingEveningTime;
}
