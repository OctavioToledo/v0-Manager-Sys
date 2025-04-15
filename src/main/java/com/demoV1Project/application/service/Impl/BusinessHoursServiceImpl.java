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

import java.time.LocalTime;
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
        // ===== 1. Validar todos los horarios primero =====
        requests.forEach(this::validateBusinessHours); // Lanza excepción si hay errores

        // ===== 2. Eliminar horarios existentes =====
        businessHoursDao.deleteByBusinessId(businessId);

        // ===== 3. Convertir DTOs a entidades y asociar businessId =====
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new IllegalArgumentException("Business not found with ID: " + businessId));

        List<BusinessHours> hoursToSave = requests.stream()
                .map(dto -> {
                    BusinessHours entity = businessHoursMapper.toEntity(dto);
                    entity.setBusiness(business);
                    return entity;
                })
                .collect(Collectors.toList());

        // ===== 4. Guardar =====
        List<BusinessHours> savedEntities = businessHoursDao.saveAll(hoursToSave);

        // ===== 5. Devolver DTOs =====
        return businessHoursMapper.toDtoList(savedEntities);
    }

    // Método de validación (puede ser privado dentro del Service)
    private void validateBusinessHours(BusinessHoursDto dto) {
        // Validar horario matutino
        LocalTime openingMorning = LocalTime.parse(dto.getOpeningMorningTime());
        LocalTime closingMorning = LocalTime.parse(dto.getClosingMorningTime());
        if (openingMorning.isAfter(closingMorning)) {
            throw new IllegalArgumentException(
                    "Horario matutino inválido para " + dto.getDayOfWeek() + ": " +
                            openingMorning + " debe ser antes de " + closingMorning
            );
        }

        // Validar horario vespertino (si aplica)
        if (dto.getOpeningEveningTime() != null && dto.getClosingEveningTime() != null) {
            LocalTime openingEvening = LocalTime.parse(dto.getOpeningEveningTime());
            LocalTime closingEvening = LocalTime.parse(dto.getClosingEveningTime());
            if (openingEvening.isAfter(closingEvening)) {
                throw new IllegalArgumentException(
                        "Horario vespertino inválido para " + dto.getDayOfWeek() + ": " +
                                openingEvening + " debe ser antes de " + closingEvening
                );
            }
            // Validar que no se solapen (opcional)
            if (!closingMorning.isBefore(openingEvening)) {
                throw new IllegalArgumentException(
                        "Intervalo inválido entre horarios para " + dto.getDayOfWeek() + ": " +
                                "El cierre matutino (" + closingMorning + ") debe ser antes de la apertura vespertina (" + openingEvening + ")"
                );
            }
        }
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