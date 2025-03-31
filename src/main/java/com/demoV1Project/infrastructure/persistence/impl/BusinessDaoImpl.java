package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
import com.demoV1Project.domain.model.Business;

import com.demoV1Project.domain.model.Category;
import com.demoV1Project.infrastructure.persistence.BusinessDao;
import com.demoV1Project.domain.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BusinessDaoImpl implements BusinessDao {

    @Autowired
    private final BusinessRepository businessRepository;

    @Autowired
    private final BusinessMapper businessMapper;

    public BusinessDaoImpl(BusinessRepository businessRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }


    @Override
    public List<Business> findAll() {
        return businessRepository.findAllWithRelations();
    }

    @Override
    public List<BusinessDto> searchBusinesses(String name, Category category, String city) {
        List<Business> businesses = businessRepository.searchBusinesses(name, category, city);
        return businesses.stream()
                .map(businessMapper::toDto)
                .collect(Collectors.toList());
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
