package com.demoV1Project.service;

import com.demoV1Project.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> findAll();
    Optional<Service> findById(Long id);
    void save(Service service);
    void deleteById(Long id);

}
