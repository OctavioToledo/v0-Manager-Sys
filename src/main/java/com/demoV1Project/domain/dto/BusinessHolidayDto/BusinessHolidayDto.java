package com.demoV1Project.domain.dto.BusinessHolidayDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessHolidayDto {
    private Long id;
    private String holidayDate;
    private String description;
    private Boolean isFullDay;
    private String startTime;
    private String endTime;
}
