package com.demoV1Project.domain.dto.BusinessDto;


import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.domain.model.User;
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
    private List<BusinessHours> businessHours;
    private String workDays;
    private Long userId;
    private Category category;
    private Address address;

}
