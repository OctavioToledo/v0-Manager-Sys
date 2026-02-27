package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.BusinessDto.BusinessShortDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BusinessService {

    List<Business> findAll();

    Page<Business> findAll(Pageable pageable);

    Optional<Business> findById(Long id);

    Business save(Business business);

    void deleteById(Long id);

    List<BusinessShortDto> searchBusinesses(String name, Category category, String city);

}
