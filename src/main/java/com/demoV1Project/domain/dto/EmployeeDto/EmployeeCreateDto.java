package com.demoV1Project.domain.dto.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String profilePicture;
    private Long businessId;
    private List<Long> serviceIds;
}
