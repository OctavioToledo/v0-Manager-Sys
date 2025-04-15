package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.BusinessHoursMapper;
import com.demoV1Project.application.service.BusinessHoursService;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.repository.BusinessRepository;
import com.demoV1Project.infrastructure.persistence.BusinessHoursDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessHoursServiceImpl implements BusinessHoursService {
    private final BusinessHoursDao businessHoursDao;
    private final BusinessHoursMapper businessHoursMapper;
    private final BusinessRepository businessRepository;

    @Override
    @Transactional
    public List<BusinessHoursDto> saveHoursForBusiness(Long businessId, List<BusinessHoursDto> requests) {
        // 1. Eliminar horarios existentes
        businessHoursDao.deleteByBusinessId(businessId);

        // 2. Convertir DTOs a entidades y asociar businessId
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Not found " + businessId));

        List<BusinessHours> hoursToSave = requests.stream()
                .map(dto -> {
                    BusinessHours entity = businessHoursMapper.toEntity(dto);
                    entity.setBusiness(business); // ← Reutiliza la misma instancia
                    return entity;
                })
                .collect(Collectors.toList());

        // 3. Guardar usando DAO
        List<BusinessHours> savedEntities = businessHoursDao.saveAll(hoursToSave);

        // 4. Devolver DTOs
        return businessHoursMapper.toDtoList(savedEntities);
    }

    @Override
    public List<BusinessHoursDto> findByBusinessId(Long businessId) {
        return businessHoursMapper.toDtoList(businessHoursDao.findByBusinessId(businessId));
    }

    @Override
    @Transactional
    public void deleteByBusinessId(Long businessId) {
        businessHoursDao.deleteByBusinessId(businessId);
    }
}