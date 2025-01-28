package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.TransactionMapper;
import com.demoV1Project.application.service.AppointmentService;
import com.demoV1Project.application.service.BusinessService;
import com.demoV1Project.application.service.TransactionService;
import com.demoV1Project.domain.dto.TransactionDto;
import com.demoV1Project.domain.model.Appointment;
import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AppointmentService appointmentService;
    private final BusinessService businessService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<TransactionDto>> findAll() {
        List<Transaction> transactions = transactionService.findAll();
        return ResponseEntity.ok(transactionMapper.toDtoList(transactions));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TransactionDto> findById(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(transactionMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody TransactionDto transactionDto) throws URISyntaxException {
        if (transactionDto.getAppointmentId() == null || transactionDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Appointment ID and Business ID are required");
        }

        Optional<Appointment> appointmentOptional = appointmentService.findById(transactionDto.getAppointmentId());
        Optional<Business> businessOptional = businessService.findById(transactionDto.getBusinessId());

        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Appointment not found");
        }

        if (businessOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Business not found");
        }

        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transaction.setAppointment(appointmentOptional.get());
        transaction.setBusiness(businessOptional.get());

        transactionService.save(transaction);

        return ResponseEntity.created(new URI("/api/v0/transaction/save")).body("Transaction saved successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (transactionService.findById(id).isPresent()) {
            transactionService.deleteById(id);
            return ResponseEntity.ok("Transaction deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
    }
}
