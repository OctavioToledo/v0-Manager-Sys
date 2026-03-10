package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.BusinessHoursMapper;
import com.demoV1Project.application.service.BusinessHoursService;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.repository.BusinessHoursRepository;
import com.demoV1Project.domain.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessHoursServiceImpl implements BusinessHoursService {
    private final BusinessHoursMapper businessHoursMapper;
    private final BusinessRepository businessRepository;
    private final BusinessHoursRepository businessHoursRepository;

    @Override
    @Transactional
    public List<BusinessHoursDto> saveHoursForBusiness(Long businessId, List<BusinessHoursDto> requests) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));
        requests.forEach(this::validateBusinessHours);

        // Eliminar horarios existentes
        businessHoursRepository.deleteByBusinessId(businessId);

        // Convertir DTOs a entidades y asignar business
        List<BusinessHours> hoursToSave = requests.stream()
                .map(dto -> {
                    BusinessHours entity = businessHoursMapper.toEntity(dto);
                    entity.setBusiness(business);
                    return entity;
                })
                .collect(Collectors.toList());

        // Guardar y retornar DTOs
        List<BusinessHours> savedEntities = businessHoursRepository.saveAll(hoursToSave);
        return businessHoursMapper.toDtoList(savedEntities);
    }

    private void validateBusinessHours(BusinessHoursDto dto) {
        if (dto.getMorningStart() == null || dto.getMorningEnd() == null) {
            throw new IllegalArgumentException("Los horarios matutinos son obligatorios");
        }

        LocalTime openingMorning = dto.getMorningStart();
        LocalTime closingMorning = dto.getMorningEnd();

        if (openingMorning.isAfter(closingMorning)) {
            throw new IllegalArgumentException("Horario matutino inválido");
        }

        if (dto.getAfternoonStart() != null && dto.getAfternoonEnd() != null) {
            LocalTime openingEvening = dto.getAfternoonStart();
            LocalTime closingEvening = dto.getAfternoonEnd();

            if (openingEvening.isAfter(closingEvening)) {
                throw new IllegalArgumentException("Horario vespertino inválido");
            }
        }
    }

    @Override
    public List<BusinessHoursDto> findByBusinessId(Long businessId) {
        return businessHoursMapper.toDtoList(businessHoursRepository.findByBusinessId(businessId));
    }

    @Override
    @Transactional
    public void deleteByBusinessId(Long businessId) {
        businessHoursRepository.deleteByBusinessId(businessId);
    }

}