package com.demoV1Project.domain.dto.BusinessDto;

import com.demoV1Project.domain.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessShortDto {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String logo;
    private Address address;
}
