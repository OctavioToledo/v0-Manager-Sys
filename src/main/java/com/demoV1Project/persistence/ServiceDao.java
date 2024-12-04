package com.demoV1Project.persistence;


import com.demoV1Project.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceDao {

    List<Service> findAll();
    Optional<Service> findById(Long id);
    void save(Service service);
    void deleteById(Long id);

}
