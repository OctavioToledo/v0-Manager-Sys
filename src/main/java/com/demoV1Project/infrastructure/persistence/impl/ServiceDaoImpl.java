package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.Service;
import com.demoV1Project.infrastructure.persistence.ServiceDao;
import com.demoV1Project.domain.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ServiceDaoImpl implements ServiceDao {

    @Autowired
    private final ServiceRepository serviceRepository;

    public ServiceDaoImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }


    @Override
    public List<Service> findByBusinessId(Long businessId) {
        return serviceRepository.findByBusinessId(businessId);
    }

    @Override
    public List<Service> findAllById(List<Long> ids) {
        return serviceRepository.findAllById(ids);
    }


    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public void save(Service service) {
        serviceRepository.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }
}
