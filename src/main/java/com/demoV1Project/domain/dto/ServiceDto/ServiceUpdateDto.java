package com.demoV1Project.domain.dto.ServiceDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceUpdateDto {
    private String name;
    private Integer duration;
    private String description;
    private Double price;
}
