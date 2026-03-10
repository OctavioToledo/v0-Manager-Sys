package com.demoV1Project.domain.dto.BusinessHours;

import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {

        private Integer dayOfWeek;
        private Boolean isWorkingDay;
        private LocalTime morningStart;
        private LocalTime morningEnd;
        private LocalTime afternoonStart;
        private LocalTime afternoonEnd;
}
