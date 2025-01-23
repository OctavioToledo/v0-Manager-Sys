package com.demoV1Project.controllers;

import com.demoV1Project.dto.TransactionDto;
import com.demoV1Project.model.Appointment;
import com.demoV1Project.model.Business;
import com.demoV1Project.model.Transaction;
import com.demoV1Project.service.AppointmentService;
import com.demoV1Project.service.BusinessService;
import com.demoV1Project.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final TransactionService transactionService;

    @Autowired
    private final AppointmentService appointmentService;

    @Autowired
    private final BusinessService businessService;



    @GetMapping("/findAll")
        public ResponseEntity<?> findAll() {
            List<TransactionDto> transactionList = transactionService.findAll()
                    .stream()
                    .map(transaction -> TransactionDto.builder()
                            .id(transaction.getId())
                            .amount(transaction.getAmount())
                            .date(transaction.getDate())
                            .description(transaction.getDescription())
                            .appointmentId(transaction.getAppointment().getId())
                            .businessId(transaction.getBusiness().getId())
                            .build())
                    .toList();
            return ResponseEntity.ok(transactionList);
        }

        @GetMapping("/find/{id}")
        public ResponseEntity<?> findById(@PathVariable Long id) {
            Optional<Transaction> transactionOptional = transactionService.findById(id);

            if (transactionOptional.isPresent()) {
                Transaction transaction = transactionOptional.get();
                TransactionDto transactionDto = TransactionDto.builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .date(transaction.getDate())
                        .description(transaction.getDescription())
                        .appointmentId(transaction.getAppointment().getId())
                        .businessId(transaction.getBusiness().getId())
                        .build();
                return ResponseEntity.ok(transactionDto);
            }
            return ResponseEntity.notFound().build();
        }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody TransactionDto transactionDto) throws URISyntaxException {
        // Validación del Appointment
        if (transactionDto.getAppointmentId() == null) {
            return ResponseEntity.badRequest().body("Appointment ID is required");
        }

        Optional<Appointment> appointmentOptional = appointmentService.findById(transactionDto.getAppointmentId());
        if (appointmentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Appointment not found");
        }
        Appointment appointment = appointmentOptional.get();

        // Validación del Business
        if (transactionDto.getBusinessId() == null) {
            return ResponseEntity.badRequest().body("Business ID is required");
        }

        Optional<Business> businessOptional = businessService.findById(transactionDto.getBusinessId());
        if (businessOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Business not found");
        }
        Business business = businessOptional.get();

        // Crear la transacción
        Transaction transaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .date(transactionDto.getDate())
                .description(transactionDto.getDescription())
                .appointment(appointment) // Asociar el Appointment existente
                .business(business)       // Asociar el Business existente
                .build();

        // Guardar la transacción
        transactionService.save(transaction);

        return ResponseEntity.created(new URI("/api/v0/transaction/save")).build();
    }


    @DeleteMapping("/delete/{id}")
        public ResponseEntity<?> deleteById(@PathVariable Long id) {
            if (id != null) {
                transactionService.deleteById(id);
                return ResponseEntity.ok("Transaction Deleted");
            }
            return ResponseEntity.badRequest().build();
        }
    }


