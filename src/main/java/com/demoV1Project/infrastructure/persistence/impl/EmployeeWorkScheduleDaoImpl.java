package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.EmployeeWorkSchedule;
import com.demoV1Project.domain.repository.EmployeeWorkScheduleRepository;
import com.demoV1Project.infrastructure.persistence.EmployeeWorkScheduleDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class EmployeeWorkScheduleDaoImpl implements EmployeeWorkScheduleDao {
        private final EmployeeWorkScheduleRepository repository;

        @Override
        public List<EmployeeWorkSchedule> saveAll(List<EmployeeWorkSchedule> schedules) {
            return repository.saveAll(schedules);
        }

        @Override
        public List<EmployeeWorkSchedule> findByEmployeeId(Long employeeId) {
            return repository.findByEmployeeId(employeeId);
        }

        @Override
        public void deleteByEmployeeId(Long employeeId) {
            repository.deleteByEmployeeId(employeeId);
        }
    }