package com.demoV1Project.service.Impl;

import com.demoV1Project.model.Address;
import com.demoV1Project.persistence.AddressDao;
import com.demoV1Project.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private final AddressDao addressDao;

    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }


    @Override
    public Optional<Address> findById(Long id) {
        return addressDao.findById(id);
    }

    @Override
    public void save(Address address) {
        addressDao.save(address);
    }

    @Override
    public void deleteById(Long id) {
        addressDao.deleteById(id);
    }
}
