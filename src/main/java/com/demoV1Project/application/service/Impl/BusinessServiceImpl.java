package com.demoV1Project.application.service.Impl;

import com.demoV1Project.domain.model.Business;
import com.demoV1Project.infrastructure.persistence.BusinessDao;
import com.demoV1Project.application.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private final BusinessDao businessDao;

    public BusinessServiceImpl(BusinessDao businessDao) {
        this.businessDao = businessDao;
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
    public void save(Business business) {
        businessDao.save(business);
    }

    @Override
    public void deleteById(Long id) {
        businessDao.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
