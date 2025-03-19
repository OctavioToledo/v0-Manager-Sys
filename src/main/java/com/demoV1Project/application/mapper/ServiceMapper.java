package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.ServiceDto.ServiceCreateDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceUpdateDto;
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

    public Service toEntity(ServiceCreateDto serviceCreateDto) {
        return modelMapper.map(serviceCreateDto, Service.class);
    }

    public void updateEntity(ServiceUpdateDto serviceUpdateDto, Service service){
        modelMapper.map(serviceUpdateDto, service);
    }

    public List<ServiceDto> toDtoList(List<Service> services) {
        return services.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
