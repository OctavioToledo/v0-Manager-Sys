package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.Address;
import com.demoV1Project.infrastructure.persistence.AddressDao;
import com.demoV1Project.domain.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressDaoImpl implements AddressDao {

    @Autowired
    private final AddressRepository addressRepository;

    public AddressDaoImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }
}
