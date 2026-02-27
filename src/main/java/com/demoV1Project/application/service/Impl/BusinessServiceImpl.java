package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.BusinessMapper;
import com.demoV1Project.domain.dto.BusinessDto.BusinessShortDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.application.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Override
    public List<Business> findAll() {
        return businessRepository.findAllWithRelations();
    }

    @Override
    public Page<Business> findAll(Pageable pageable) {
        return businessRepository.findAll(pageable);
    }

    @Override
    public Optional<Business> findById(Long id) {
        return businessRepository.findById(id);
    }

    @Override
    public Business save(Business business) {
        return businessRepository.save(business);
    }

    @Override
    public void deleteById(Long id) {
        businessRepository.deleteById(id);
    }

    @Override
    public List<BusinessShortDto> searchBusinesses(String name, Category category, String city) {
        List<Business> businesses = businessRepository.searchBusinesses(name, category, city);
        return businesses.stream()
                .map(businessMapper::toShortDto)
                .collect(Collectors.toList());
    }

}
