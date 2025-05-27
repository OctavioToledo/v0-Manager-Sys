package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.dto.AddressDto.AddressCreateDto;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BusinessRegistrationDto {
    private BusinessCreateDto business;
    private AddressCreateDto address;
    private List<BusinessHoursDto> businessHours;
    private Long categoryId;
    private Long userId; // ID del User
}

