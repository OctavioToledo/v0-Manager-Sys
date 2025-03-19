package com.demoV1Project.domain.dto.EmployeeDto;

import com.demoV1Project.domain.dto.ServiceDto.ServiceShortDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmployeeDetailDto {
    private Long id;
    private String name;
    private String profilePicture;
    private List<ServiceShortDto> services;
}
