package com.demoV1Project.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String workSchedule;
    private String profilePicture;
    private Long businessId;
    private List<Long> serviceIds;
}

