package com.demoV1Project.domain.dto.ServiceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceShortDto {
    private Long id;
    private String name;
    private Double price;
}
