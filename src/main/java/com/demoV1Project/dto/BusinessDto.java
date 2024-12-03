package com.demoV1Project.dto;


import com.demoV1Project.model.Address;
import com.demoV1Project.model.Category;
import com.demoV1Project.model.User;
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
    private Long userId;
    private User user;
    private Address address;
    private Category category;
}
