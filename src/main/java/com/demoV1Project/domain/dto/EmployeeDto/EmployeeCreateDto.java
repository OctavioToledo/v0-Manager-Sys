package com.demoV1Project.domain.dto.EmployeeDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EmployeeCreateDto {
    private String name;
    private String workSchedule;
    private String profilePicture;
    private Long businessId;
    private List<Long> serviceIds;
}
