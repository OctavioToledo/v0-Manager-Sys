package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import com.demoV1Project.domain.model.Employee;
import com.demoV1Project.domain.model.Service;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceMapper {

    private final ModelMapper modelMapper;

    public ServiceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ServiceDto toDto(Service service) {
        ServiceDto serviceDto = modelMapper.map(service, ServiceDto.class);
        serviceDto.setBusinessId(service.getBusiness().getId());
        serviceDto.setEmployeeIds(service.getEmployees()
                .stream()
                .map(Employee::getId)
                .collect(Collectors.toList()));
        return serviceDto;
    }

    public Service toEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, Service.class);
    }

    public List<ServiceDto> toDtoList(List<Service> services) {
        return services.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
