package com.demoV1Project.persistence.impl;

import com.demoV1Project.model.Business;

import com.demoV1Project.persistence.BusinessDao;
import com.demoV1Project.repositories.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BusinessDaoImpl implements BusinessDao {

    @Autowired
    private final BusinessRepository businessRepository;

    public BusinessDaoImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }


    @Override
    public List<Business> findAll() {
        return businessRepository.findAllWithRelations();
    }

    @Override
    public Optional<Business> findById(Long id) {
        return businessRepository.findById(id);
    }

    @Override
    public void save(Business business) {
        businessRepository.save(business);
    }

    @Override
    public void deleteById(Long id) {
        businessRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
