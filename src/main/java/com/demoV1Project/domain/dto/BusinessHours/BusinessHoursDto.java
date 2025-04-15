package com.demoV1Project.domain.dto.BusinessHours;

import com.demoV1Project.util.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHoursDto {

        private DayOfWeek dayOfWeek;
        private String openingTime; // Formato HH:mm (ej: "09:00")
        private String closingTime;

}
