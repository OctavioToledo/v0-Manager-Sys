package com.demoV1Project.persistence;

import com.demoV1Project.model.Address;

import java.util.Optional;

public interface AddressDao {
    Optional<Address> findById(Long id);
    void save(Address address);
    // void update(Address address);
    void deleteById(Long id);
}
