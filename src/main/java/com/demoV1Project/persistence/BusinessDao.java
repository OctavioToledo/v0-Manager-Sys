package com.demoV1Project.persistence;

import com.demoV1Project.model.Business;


import java.util.List;
import java.util.Optional;

public interface BusinessDao {

    List<Business> findAll();
    Optional<Business> findById(Long id);
    void save(Business business);
    void deleteById(Long id);
    boolean existsById(Long id);


}
