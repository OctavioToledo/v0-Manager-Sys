package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.ServiceMapper;
import com.demoV1Project.domain.dto.ServiceDto.ServiceShortDto;
import com.demoV1Project.domain.model.Service;
import com.demoV1Project.domain.repository.ServiceRepository;
import com.demoV1Project.application.service.ServiceService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public List<ServiceShortDto> findByBusinessId(Long businessId) {
        List<Service> services = serviceRepository.findByBusinessId(businessId);
        return serviceMapper.toShortDtoList(services);
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
    public Service save(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
    }
}
