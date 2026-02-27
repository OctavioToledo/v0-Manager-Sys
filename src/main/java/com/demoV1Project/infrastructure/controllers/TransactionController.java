package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.TransactionMapper;
import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.TransactionService;
import com.demoV1Project.domain.dto.TransactionDto.TransactionCreateDto;
import com.demoV1Project.domain.dto.TransactionDto.TransactionDto;
import com.demoV1Project.domain.dto.TransactionDto.TransactionUpdateDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Transaction;
import com.demoV1Project.shared.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AppointmentService appointmentService;
    private final BusinessService businessService;
    private final TransactionMapper transactionMapper;
    private final TenantContext tenantContext;

    @GetMapping("/findAll")
    public ResponseEntity<org.springframework.data.domain.Page<TransactionDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page, size, org.springframework.data.domain.Sort.by("createdAt").descending());
        org.springframework.data.domain.Page<TransactionDto> result = transactionService.findAll(pageable)
                .map(transactionMapper::toDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TransactionDto> findById(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(transaction -> {
                    tenantContext.validateBusinessOwnership(transaction.getBusiness().getId());
                    return ResponseEntity.ok(transactionMapper.toDto(transaction));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody TransactionCreateDto transactionDto) throws URISyntaxException {
        if (transactionDto.getAppointmentId() == null || transactionDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Appointment ID and Business ID are required");
        }
        tenantContext.validateBusinessOwnership(transactionDto.getBusinessId());

        Appointment appointment = appointmentService.findById(transactionDto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        Business business = businessService.findById(transactionDto.getBusinessId())
                .orElseThrow(() -> new IllegalArgumentException("Business not found"));

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setAppointment(appointment);
        transaction.setBusiness(business);

        transactionService.save(transaction);

        return ResponseEntity.created(new URI("/api/v1/transaction/save")).body("Transaction saved successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
            @RequestBody TransactionUpdateDto transactionUpdateDto) {
        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        tenantContext.validateBusinessOwnership(transaction.getBusiness().getId());
        transactionMapper.updateEntity(transactionUpdateDto, transaction);
        transactionService.save(transaction);
        return ResponseEntity.ok("Transaction updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        tenantContext.validateBusinessOwnership(transaction.getBusiness().getId());
        transactionService.deleteById(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }
}
