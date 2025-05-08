package com.demoV1Project.domain.dto.EmployeeDto;

import com.demoV1Project.domain.dto.ServiceDto.ServiceShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String profilePicture;
   // private List<ServiceShortDto> services;
}
