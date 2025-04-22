package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.exceptions.InvalidEmployeeWorkScheduleException;
import com.demoV1Project.application.mapper.EmployeeWorkScheduleMapper;
import com.demoV1Project.application.service.EmployeeWorkScheduleService;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;
import com.demoV1Project.domain.model.BusinessHours;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import com.demoV1Project.domain.repository.BusinessHoursRepository;
import com.demoV1Project.domain.repository.EmployeeRepository;
import com.demoV1Project.infrastructure.persistence.EmployeeWorkScheduleDao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeWorkScheduleServiceImpl implements EmployeeWorkScheduleService {
    private final EmployeeWorkScheduleDao employeeWorkScheduleDao;
    private final EmployeeRepository employeeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final EmployeeWorkScheduleMapper mapper;

    @Override
    @Transactional
    public List<EmployeeWorkScheduleDto> saveAllForEmployee(Long employeeId, List<EmployeeWorkScheduleRequest> requests) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // 1. Eliminar horarios existentes
        employeeWorkScheduleDao.deleteByEmployeeId(employeeId);

        // 2. Obtener los BusinessHours del negocio
        List<BusinessHours> businessHours = businessHoursRepository.findByBusinessId(employee.getBusiness().getId());
        if (businessHours.isEmpty()) {
            throw new InvalidEmployeeWorkScheduleException("El negocio no tiene horarios configurados");
        }
        // 3. Validar cada EmployeeWorkSchedule
        requests.forEach(request -> validateAgainstBusinessHours(request, businessHours));

        // 2. Convertir DTOs a entidades
        List<EmployeeWorkSchedule> schedules = requests.stream()
                .map(request -> {
                    EmployeeWorkSchedule schedule = mapper.toEntity(request);
                    schedule.setEmployee(employee);
                    return schedule;
                })
                .toList();

        // 3. Guardar usando DAO
        List<EmployeeWorkSchedule> savedSchedules = employeeWorkScheduleDao.saveAll(schedules);
        return mapper.toDtoList(savedSchedules);
    }

    @Override
    public List<EmployeeWorkScheduleDto> findByEmployeeId(Long employeeId) {
        return mapper.toDtoList(employeeWorkScheduleDao.findByEmployeeId(employeeId));
    }

    @Override
    @Transactional
    public void deleteByEmployeeId(Long employeeId) {
        employeeWorkScheduleDao.deleteByEmployeeId(employeeId);
    }
    private void validateAgainstBusinessHours(EmployeeWorkScheduleRequest request, List<BusinessHours> businessHours) {
        // Buscar el BusinessHours para el día específico
        BusinessHours businessHourForDay = businessHours.stream()
                .filter(bh -> bh.getDayOfWeek() == request.getDayOfWeek())
                .findFirst()
                .orElseThrow(() -> new InvalidEmployeeWorkScheduleException(
                        "El negocio no trabaja el día " + request.getDayOfWeek()
                ));

        // Convertir horarios a LocalTime (si el DTO usa String)
        LocalTime empMorningStart = LocalTime.parse(request.getOpeningMorningTime());
        LocalTime empMorningEnd = LocalTime.parse(request.getClosingMorningTime());
        LocalTime empEveningStart = request.getOpeningEveningTime() != null ?
                LocalTime.parse(request.getOpeningEveningTime()) : null;
        LocalTime empEveningEnd = request.getClosingEveningTime() != null ?
                LocalTime.parse(request.getClosingEveningTime()) : null;

        // Validar horario matutino
        if (empMorningStart.isBefore(businessHourForDay.getOpeningMorningTime()) ||
                empMorningEnd.isAfter(businessHourForDay.getClosingMorningTime())) {
            throw new InvalidEmployeeWorkScheduleException(
                    "Horario matutino del empleado debe estar entre " +
                            businessHourForDay.getOpeningMorningTime() + " y " +
                            businessHourForDay.getClosingMorningTime()
            );
        }

        // Validar horario vespertino (si existe)
        if (empEveningStart != null && empEveningEnd != null) {
            if (businessHourForDay.getOpeningEveningTime() == null ||
                    businessHourForDay.getClosingEveningTime() == null) {
                throw new InvalidEmployeeWorkScheduleException(
                        "El negocio no tiene horario vespertino configurado para " + request.getDayOfWeek()
                );
            }
            if (empEveningStart.isBefore(businessHourForDay.getOpeningEveningTime()) ||
                    empEveningEnd.isAfter(businessHourForDay.getClosingEveningTime())) {
                throw new InvalidEmployeeWorkScheduleException(
                        "Horario vespertino del empleado debe estar entre " +
                                businessHourForDay.getOpeningEveningTime() + " y " +
                                businessHourForDay.getClosingEveningTime()
                );
}}}}