package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.SubscriptionPaymentDto;
import com.demoV1Project.domain.model.SubscriptionPayment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionPaymentMapper {

    private final ModelMapper modelMapper;

    public SubscriptionPaymentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SubscriptionPaymentDto toDto(SubscriptionPayment payment) {
        SubscriptionPaymentDto dto = modelMapper.map(payment, SubscriptionPaymentDto.class);
        dto.setUserId(payment.getUser().getId());
        return dto;
    }

    public SubscriptionPayment toEntity(SubscriptionPaymentDto dto) {
        return modelMapper.map(dto, SubscriptionPayment.class);
    }

    public List<SubscriptionPaymentDto> toDtoList(List<SubscriptionPayment> payments) {
        return payments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
