package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.ServiceMapper;
import com.demoV1Project.domain.dto.ServiceDto.ServiceShortDto;
import com.demoV1Project.domain.model.Service;
import com.demoV1Project.infrastructure.persistence.ServiceDao;
import com.demoV1Project.application.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private final ServiceDao serviceDao;

    @Autowired
    private final ServiceMapper serviceMapper;


    public ServiceServiceImpl(ServiceDao serviceDao, ServiceMapper serviceMapper) {
        this.serviceDao = serviceDao;
        this.serviceMapper = serviceMapper;
    }


    @Override
    public List<ServiceShortDto> findByBusinessId(Long businessId) {
        List<Service> services = serviceDao.findByBusinessId(businessId);
        return serviceMapper.toShortDtoList(services);
    }

    @Override
    public List<Service> findAllById(List<Long> ids) {
        return serviceDao.findAllById(ids);
    }


    @Override
    public List<com.demoV1Project.domain.model.Service> findAll() {
        return serviceDao.findAll();
    }

    @Override
    public Optional<com.demoV1Project.domain.model.Service> findById(Long id) {
        return serviceDao.findById(id);
    }

    @Override
    public void save(com.demoV1Project.domain.model.Service service) {
        serviceDao.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceDao.deleteById(id);
    }
}
