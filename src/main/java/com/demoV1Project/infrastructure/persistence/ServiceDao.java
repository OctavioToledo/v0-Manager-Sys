package com.demoV1Project.infrastructure.persistence;


import com.demoV1Project.domain.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceDao {

    List<Service> findAll();
    Optional<Service> findById(Long id);
    void save(Service service);
    void deleteById(Long id);

}
