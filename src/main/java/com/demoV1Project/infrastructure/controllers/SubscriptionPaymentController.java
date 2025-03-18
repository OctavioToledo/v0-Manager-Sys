package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.mapper.SubscriptionPaymentMapper;
import com.demoV1Project.application.service.SubscriptionPaymentService;
import com.demoV1Project.application.service.UserService;
import com.demoV1Project.domain.dto.SubscriptionPaymentDto.SubscriptionPaymentDto;
import com.demoV1Project.domain.model.SubscriptionPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v0/subscription")
@RequiredArgsConstructor
public class SubscriptionPaymentController {

    private final SubscriptionPaymentService paymentService;
    private final UserService userService;
    private final SubscriptionPaymentMapper paymentMapper;

    @GetMapping("/findAll")
    public ResponseEntity<List<SubscriptionPaymentDto>> findAll() {
        List<SubscriptionPayment> payments = paymentService.findAll();
        return ResponseEntity.ok(paymentMapper.toDtoList(payments));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<SubscriptionPaymentDto> findById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(paymentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody SubscriptionPaymentDto paymentDto) throws URISyntaxException {
        if (paymentDto.getUserId() == null) {
            return ResponseEntity.badRequest().body("User ID is required");
        }

        return userService.findById(paymentDto.getUserId())
                .map(user -> {
                    SubscriptionPayment payment = paymentMapper.toEntity(paymentDto);
                    payment.setUser(user);
                    paymentService.save(payment);
                    return ResponseEntity.created(URI.create("/api/v0/subscription/save")).body("Payment saved successfully");
                })
                .orElse(ResponseEntity.badRequest().body("User not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        if (paymentService.findById(id).isPresent()) {
            paymentService.deleteById(id);
            return ResponseEntity.ok("Payment deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
    }
}
