package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.dto.BusinessDto.BusinessDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;


import java.util.List;
import java.util.Optional;

public interface BusinessDao {

    List<Business> findAll();
    Optional<Business> findById(Long id);
    void save(Business business);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Business> searchBusinesses(String name, Category category, String city);


}
