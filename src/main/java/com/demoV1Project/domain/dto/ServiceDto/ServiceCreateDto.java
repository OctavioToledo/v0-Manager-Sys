package com.demoV1Project.domain.dto.ServiceDto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ServiceCreateDto {
    private String name;
    private Integer duration;
    private String description;
    private Double price;
    private Long businessId;
}
