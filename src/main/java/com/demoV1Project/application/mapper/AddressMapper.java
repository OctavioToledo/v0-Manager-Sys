package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AddressDto;
import com.demoV1Project.domain.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    @Autowired
    public ModelMapper modelMapper;

    public AddressDto toDto(Address address){
        return modelMapper.map(address, AddressDto.class);
    }
    public Address toEntity(AddressDto addressDto){
        return modelMapper.map(addressDto, Address.class);
    }


}
