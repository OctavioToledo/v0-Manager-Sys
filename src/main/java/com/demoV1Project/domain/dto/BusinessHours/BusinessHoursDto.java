package com.demoV1Project.domain.dto.BusinessHours;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {

        private Long id;

        @JsonProperty("day_of_week")
        private String dayOfWeekName; // String like "Lunes"

        private Integer dayOfWeek;
        private Boolean isWorkingDay;

        @JsonProperty("opening_morning_time")
        private LocalTime morningStart;

        @JsonProperty("closing_morning_time")
        private LocalTime morningEnd;

        @JsonProperty("opening_evening_time")
        private LocalTime afternoonStart;

        @JsonProperty("closing_evening_time")
        private LocalTime afternoonEnd;
}
