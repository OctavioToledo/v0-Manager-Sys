package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.Address;

import java.util.Optional;

public interface AddressService {
    Optional<Address> findById(Long id);
    void save(Address address);
    // void update(Address address);
    void deleteById(Long id);
}
