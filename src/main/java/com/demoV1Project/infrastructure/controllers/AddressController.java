package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AddressMapper;
import com.demoV1Project.domain.dto.AddressDto;
import com.demoV1Project.domain.model.Address;
import com.demoV1Project.application.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/address")
@RequiredArgsConstructor
public class AddressController {

    @Autowired
    private final AddressService addressService;

    @Autowired
    private final AddressMapper addressMapper;


    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Address> addressOptional = addressService.findById(id);

        if (addressOptional.isPresent()) {
            AddressDto addressDto = addressMapper.toDto(addressOptional.get());
            return ResponseEntity.ok(addressDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AddressDto addressDto) throws URISyntaxException {
        Address address = addressMapper.toEntity(addressDto);
        addressService.save(address);
        return ResponseEntity.created(new URI("/api/v0/address/save")).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AddressDto addressDto) {

        Optional<Address> addressOptional = addressService.findById(id);

        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setStreet(addressDto.getStreet());
            address.setStreetNumber(addressDto.getStreetNumber());
            address.setCity(addressDto.getCity());
            address.setProvince(addressDto.getProvince());
            address.setCountry(addressDto.getCountry());

            addressService.save(address);

            return ResponseEntity.ok("Field Updated");
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id != null) {
            addressService.deleteById(id);
            return ResponseEntity.ok("Field Deleted");
        }
        return ResponseEntity.badRequest().build();
    }

}
