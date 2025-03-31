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
public class EmployeeUpdateDto {
    private String name;
    private String role;
    private String workSchedule;
    private String profilePicture;
    private List<Long> serviceIds;
}
