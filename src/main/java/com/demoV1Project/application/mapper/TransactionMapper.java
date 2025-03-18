package com.demoV1Project.application.mapper;

import com.demoV1Project.domain.dto.TransactionDto.TransactionDto;
import com.demoV1Project.domain.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    private final ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDto toDto(Transaction transaction) {
        TransactionDto dto = modelMapper.map(transaction, TransactionDto.class);
        dto.setAppointmentId(transaction.getAppointment() != null ? transaction.getAppointment().getId() : null);
        dto.setBusinessId(transaction.getBusiness().getId());
        return dto;
    }

    public Transaction toEntity(TransactionDto dto) {
        return modelMapper.map(dto, Transaction.class);
    }

    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
