package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.BusinessDto.*;
import com.demoV1Project.domain.dto.BusinessHours.BusinessHoursDto;
import com.demoV1Project.domain.dto.EmployeeDto.EmployeeDto;
import com.demoV1Project.domain.dto.ServiceDto.ServiceDto;
import com.demoV1Project.domain.dto.UserDto.UserShortDto;
import com.demoV1Project.domain.model.Business;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusinessMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Convertir entidad a DTO
    public BusinessDto toDto(Business business) {
        return modelMapper.map(business, BusinessDto.class);
    }

    public BusinessDetailDto toDetailDto(Business business) {
        BusinessDetailDto dto = modelMapper.map(business, BusinessDetailDto.class);
        if (business.getUser() != null) {
            dto.setOwner(new UserShortDto(business.getUser().getId(), business.getUser().getName()));
        }
        // Explicitly mapping collections to ensure nested DTOs are handled
        if (business.getEmployees() != null) {
            dto.setEmployees(business.getEmployees().stream()
                    .map(e -> {
                        EmployeeDto eDto = modelMapper.map(e, EmployeeDto.class);
                        if (e.getServices() != null) {
                            eDto.setServices(e.getServices().stream()
                                    .map(s -> modelMapper.map(s,
                                            com.demoV1Project.domain.dto.ServiceDto.ServiceDto.class))
                                    .collect(Collectors.toList()));
                        }
                        return eDto;
                    })
                    .collect(Collectors.toList()));
        }
        if (business.getServices() != null) {
            dto.setServices(business.getServices().stream()
                    .map(s -> modelMapper.map(s, com.demoV1Project.domain.dto.ServiceDto.ServiceDto.class))
                    .collect(Collectors.toList()));
        }
        if (business.getBusinessHours() != null) {
            dto.setBusinessHours(business.getBusinessHours().stream()
                    .map(this::toBusinessHoursDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public BusinessShortDto toShortDto(Business business) {
        BusinessShortDto dto = modelMapper.map(business, BusinessShortDto.class);
        // Explicitly mapping collections for search/list views
        if (business.getEmployees() != null) {
            dto.setEmployees(business.getEmployees().stream()
                    .map(e -> modelMapper.map(e, EmployeeDto.class))
                    .collect(Collectors.toList()));
        }
        if (business.getServices() != null) {
            dto.setServices(business.getServices().stream()
                    .map(s -> modelMapper.map(s, com.demoV1Project.domain.dto.ServiceDto.ServiceDto.class))
                    .collect(Collectors.toList()));
        }
        if (business.getBusinessHours() != null) {
            dto.setBusinessHours(business.getBusinessHours().stream()
                    .map(this::toBusinessHoursDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private BusinessHoursDto toBusinessHoursDto(com.demoV1Project.domain.model.BusinessHours bh) {
        BusinessHoursDto dto = modelMapper.map(bh, BusinessHoursDto.class);
        dto.setDayOfWeekName(translateDay(bh.getDayOfWeek()));
        return dto;
    }

    private String translateDay(Integer day) {
        if (day == null)
            return null;
        return switch (day) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6 -> "Sábado";
            case 7 -> "Domingo";
            default -> "Desconocido";
        };
    }

    // Convertir DTO a entidad
    public Business toEntity(BusinessCreateDto businessCreateDto) {
        return modelMapper.map(businessCreateDto, Business.class);
    }

    // Convertir lista de entidades a lista de DTOs
    public List<BusinessDto> toDtoList(List<Business> businesses) {
        return businesses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void updateEntity(BusinessUpdateDto businessUpdateDto, Business business) {
        modelMapper.map(businessUpdateDto, business);
    }

}
