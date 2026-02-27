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
import java.time.format.DateTimeParseException;
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
        if (dto.getOpeningMorningTime() == null || dto.getClosingMorningTime() == null) {
            throw new IllegalArgumentException("Los horarios matutinos son obligatorios");
        }

        try {
            LocalTime openingMorning = LocalTime.parse(dto.getOpeningMorningTime());
            LocalTime closingMorning = LocalTime.parse(dto.getClosingMorningTime());

            if (openingMorning.isAfter(closingMorning)) {
                throw new IllegalArgumentException("Horario matutino inválido");
            }

            if (dto.getOpeningEveningTime() != null && dto.getClosingEveningTime() != null) {
                LocalTime openingEvening = LocalTime.parse(dto.getOpeningEveningTime());
                LocalTime closingEvening = LocalTime.parse(dto.getClosingEveningTime());

                if (openingEvening.isAfter(closingEvening)) {
                    throw new IllegalArgumentException("Horario vespertino inválido");
                }
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm");
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