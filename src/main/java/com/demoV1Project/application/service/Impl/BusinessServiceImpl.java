package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.domain.dto.BusinessDto.BusinessShortDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.infrastructure.persistence.BusinessDao;
import com.demoV1Project.application.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private final BusinessDao businessDao;

    @Autowired
    private final BusinessMapper businessMapper;

    public BusinessServiceImpl(BusinessDao businessDao, BusinessMapper businessMapper) {
        this.businessDao = businessDao;
        this.businessMapper = businessMapper;
    }


    @Override
    public List<Business> findAll() {
        return businessDao.findAll();
    }

    @Override
    public Optional<Business> findById(Long id) {
        return businessDao.findById(id);
    }

    @Override
    public Business save(Business business) {
        businessDao.save(business);
        return business;
    }

    @Override
    public void deleteById(Long id) {
        businessDao.deleteById(id);
    }

    @Override
    public List<BusinessShortDto> searchBusinesses(String name, Category category, String city) {
        List<Business> businesses = businessDao.searchBusinesses(name, category, city);
        return businesses.stream()
                .map(businessMapper::toShortDto)
                .collect(Collectors.toList());
    }

}
