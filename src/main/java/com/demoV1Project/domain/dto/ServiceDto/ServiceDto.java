package com.demoV1Project.domain.dto.ServiceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    private Long id;
    private String name;
    private Integer duration;
    private String description;
    private Double price;
    private Long businessId;
    private List<Long> employeeIds;
}

