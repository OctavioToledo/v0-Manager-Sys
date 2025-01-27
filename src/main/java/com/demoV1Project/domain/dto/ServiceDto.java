package com.demoV1Project.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ServiceDto {
    private Long id;
    private String name;
    private Integer duration;
    private String description;
    private Double price;
    private Long businessId;
    private List<Long> employeeIds;
}

