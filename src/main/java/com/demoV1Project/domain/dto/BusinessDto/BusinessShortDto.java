package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.dto.CategoryDto.CategoryDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessShortDto {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private Address address;
    private CategoryDto category;
    private List<EmployeeDto> employees;
    private List<ServiceDto> services;
    private List<BusinessHoursDto> businessHours;
}
