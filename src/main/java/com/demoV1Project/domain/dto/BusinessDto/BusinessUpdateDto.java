package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BusinessUpdateDto {
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private List<BusinessHours> businessHours;
    private String workDays;
    private Category category;
    private Address address;
}
