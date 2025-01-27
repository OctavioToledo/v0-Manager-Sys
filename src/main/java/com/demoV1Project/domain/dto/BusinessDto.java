package com.demoV1Project.domain.dto;


import com.demoV1Project.domain.model.Address;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String openingHours;
    private String workDays;
    // private Long userId;
    private Address address;
    private Category category;
    private User user;

}
