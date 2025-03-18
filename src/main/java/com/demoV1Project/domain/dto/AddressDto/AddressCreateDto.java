package com.demoV1Project.domain.dto.AddressDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressCreateDto {
    private String street;
    private String streetNumber;
    private String city;
    private String province;
    private String country;
}
