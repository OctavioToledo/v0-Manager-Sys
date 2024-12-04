package com.demoV1Project.service.Impl;

import com.demoV1Project.persistence.ServiceDao;
import com.demoV1Project.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private final ServiceDao serviceDao;

    public ServiceServiceImpl(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Override
    public List<com.demoV1Project.model.Service> findAll() {
        return serviceDao.findAll();
    }

    @Override
    public Optional<com.demoV1Project.model.Service> findById(Long id) {
        return serviceDao.findById(id);
    }

    @Override
    public void save(com.demoV1Project.model.Service service) {
        serviceDao.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceDao.deleteById(id);
    }
}
