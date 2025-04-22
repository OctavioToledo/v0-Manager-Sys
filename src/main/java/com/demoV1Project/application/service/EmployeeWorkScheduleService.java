package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleDto;
import com.demoV1Project.domain.dto.EmployeeWorkScheduleDto.EmployeeWorkScheduleRequest;

import java.util.List;

public interface EmployeeWorkScheduleService {
    List<EmployeeWorkScheduleDto> saveAllForEmployee(Long employeeId, List<EmployeeWorkScheduleRequest> requests);
    List<EmployeeWorkScheduleDto> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);
}
