package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.AddressMapper;
import com.demoV1Project.domain.dto.AddressDto;
import com.demoV1Project.domain.model.Address;
import com.demoV1Project.application.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @GetMapping("/find/{id}")
    public ResponseEntity<AddressDto> findById(@PathVariable Long id) {
        Optional<Address> addressOptional = addressService.findById(id);

        return addressOptional
                .map(address -> ResponseEntity.ok(addressMapper.toDto(address)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AddressDto addressDto) throws URISyntaxException {
        Address address = addressMapper.toEntity(addressDto);
        addressService.save(address);
        return ResponseEntity.created(new URI("/api/v0/address/save"))
                .body("Address created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        Optional<Address> addressOptional = addressService.findById(id);

        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setStreet(addressDto.getStreet());
            address.setStreetNumber(addressDto.getStreetNumber());
            address.setCity(addressDto.getCity());
            address.setProvince(addressDto.getProvince());
            address.setCountry(addressDto.getCountry());

            addressService.save(address);
            return ResponseEntity.ok("Address updated successfully");
        }

        return ResponseEntity.badRequest().body("Address not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (addressService.findById(id).isPresent()) {
            addressService.deleteById(id);
            return ResponseEntity.ok("Address deleted successfully");
        }
        return ResponseEntity.badRequest().body("Invalid ID");
    }
}
