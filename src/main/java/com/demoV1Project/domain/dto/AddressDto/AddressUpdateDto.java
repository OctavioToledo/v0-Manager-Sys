package com.demoV1Project.domain.dto.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateDto { 
    private String street;
    private String streetNumber;
    private String city;
    private String province;
    private String country;
}
