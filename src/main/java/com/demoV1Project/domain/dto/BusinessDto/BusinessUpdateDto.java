package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.Category;


public class BusinessUpdateDto {
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private String openingHours;
    private String workDays;
    private Category category;
    private Address address;
}
