package com.demoV1Project.domain.dto.EmployeeDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeShortDto {
    private Long id;
    private String name;
}
