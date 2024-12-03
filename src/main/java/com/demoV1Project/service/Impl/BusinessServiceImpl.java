package com.demoV1Project.service.Impl;

import com.demoV1Project.model.Business;
import com.demoV1Project.persistence.BusinessDao;
import com.demoV1Project.service.BusinessService;
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
