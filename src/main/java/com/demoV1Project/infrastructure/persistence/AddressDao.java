package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.model.Address;

import java.util.Optional;

public interface AddressDao {
    Optional<Address> findById(Long id);
    void save(Address address);
    // void update(Address address);
    void deleteById(Long id);
}
