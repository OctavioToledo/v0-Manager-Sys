package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.AddressDto.AddressCreateDto;
import com.demoV1Project.domain.dto.AddressDto.AddressDto;
import com.demoV1Project.domain.dto.AddressDto.AddressUpdateDto;
import com.demoV1Project.domain.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AddressDto toDto(Address address) {
        return modelMapper.map(address, AddressDto.class);
    }

    public Address toEntity(AddressCreateDto addressCreateDTO ) {
        return modelMapper.map(addressCreateDTO , Address.class);
    }

    public List<AddressDto> toDtoList(List<Address> addresses) {
        return addresses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntity(AddressUpdateDto addressUpdateDTO, Address address) {
        modelMapper.map(addressUpdateDTO, address);
    }

}
