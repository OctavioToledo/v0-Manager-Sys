package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.dto.AddressDto.AddressDto;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.dto.CategoryDto.CategoryDto;
import com.demoV1Project.domain.dto.UserDto.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDetailDto {

    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private List<BusinessHoursDto> businessHours;
    private String workDays;
    private UserShortDto owner;
    private CategoryDto category;
    private AddressDto address;

}
