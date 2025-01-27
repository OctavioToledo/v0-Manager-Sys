package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> findAll();
    Optional<Service> findById(Long id);
    void save(Service service);
    void deleteById(Long id);

}
