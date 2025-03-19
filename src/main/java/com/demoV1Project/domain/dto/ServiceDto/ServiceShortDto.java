package com.demoV1Project.domain.dto.ServiceDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceShortDto {
    private Long id;
    private String name;
    private Double price;
}
