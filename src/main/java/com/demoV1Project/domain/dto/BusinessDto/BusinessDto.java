package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDto {

    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private List<BusinessHoursDto> businessHours;
    private String workDays;
    private Long userId;
    private Category category;
    private Address address;

}
