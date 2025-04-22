package com.demoV1Project.domain.dto.BusinessHours;

import com.demoV1Project.util.enums.DayOfWeek;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.lang.Nullable;


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
