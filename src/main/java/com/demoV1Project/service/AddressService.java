package com.demoV1Project.service;

import com.demoV1Project.model.Address;

import java.util.Optional;

public interface AddressService {
    Optional<Address> findById(Long id);
    void save(Address address);
    // void update(Address address);
    void deleteById(Long id);
}
