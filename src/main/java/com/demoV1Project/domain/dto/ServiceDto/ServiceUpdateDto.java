package com.demoV1Project.domain.dto.ServiceDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateDto {
    private String name;
    private Integer duration;
    private String description;
    private Double price;
}
