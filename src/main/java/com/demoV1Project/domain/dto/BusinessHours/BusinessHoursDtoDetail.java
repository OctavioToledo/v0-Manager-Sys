package com.demoV1Project.domain.dto.BusinessHours;

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
public class BusinessHoursDtoDetail {

        private DayOfWeek dayOfWeek;
    private LocalTime openingMorningTime;
    private LocalTime closingMorningTime;
    private LocalTime openingEveningTime;
    private LocalTime closingEveningTime;

}
