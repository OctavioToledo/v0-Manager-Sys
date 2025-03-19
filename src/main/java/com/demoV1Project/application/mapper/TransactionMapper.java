package com.demoV1Project.application.mapper;


import com.demoV1Project.domain.dto.TransactionDto.TransactionCreateDto;
import com.demoV1Project.domain.dto.TransactionDto.TransactionDto;
import com.demoV1Project.domain.dto.TransactionDto.TransactionUpdateDto;

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

    public Transaction toEntity(TransactionCreateDto transactionCreateDto) {
        return modelMapper.map(transactionCreateDto, Transaction.class);
    }

    public List<TransactionDto> toDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public void updateEntity(TransactionUpdateDto TransactionUpdateDto, Transaction transaction){
        modelMapper.map(TransactionUpdateDto, transaction);
    }
}
