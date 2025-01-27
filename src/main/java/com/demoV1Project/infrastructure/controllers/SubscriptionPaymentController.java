package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.domain.dto.SubscriptionPaymentDto;
import com.demoV1Project.domain.model.SubscriptionPayment;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.application.service.SubscriptionPaymentService;
import com.demoV1Project.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v0/subscription")
@RequiredArgsConstructor
public class SubscriptionPaymentController {
    @Autowired
    private final SubscriptionPaymentService paymentService;
    @Autowired
    private final UserService userService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<SubscriptionPaymentDto> paymentList = paymentService.findAll()
                .stream()
                .map(payment -> SubscriptionPaymentDto.builder()
                        .id(payment.getId())
                        .userId(payment.getUser().getId())
                        .amount(payment.getAmount())
                        .paymentDate(payment.getPaymentDate())
                        .paymentMethod(payment.getPaymentMethod())
                        .status(payment.getStatus())
                        .description(payment.getDescription())
                        .build())
                .toList();
        return ResponseEntity.ok(paymentList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<SubscriptionPayment> paymentOptional = paymentService.findById(id);

        if (paymentOptional.isPresent()) {
            SubscriptionPayment payment = paymentOptional.get();
            SubscriptionPaymentDto paymentDto = SubscriptionPaymentDto.builder()
                    .id(payment.getId())
                    .userId(payment.getUser().getId())
                    .amount(payment.getAmount())
                    .paymentDate(payment.getPaymentDate())
                    .paymentMethod(payment.getPaymentMethod())
                    .status(payment.getStatus())
                    .description(payment.getDescription())
                    .build();
            return ResponseEntity.ok(paymentDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SubscriptionPaymentDto paymentDto) throws URISyntaxException {

        Optional<User> userOptional = userService.findById(paymentDto.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        SubscriptionPayment payment = SubscriptionPayment.builder()
                .user(userOptional.get())
                .amount(paymentDto.getAmount())
                .paymentDate(paymentDto.getPaymentDate())
                .paymentMethod(paymentDto.getPaymentMethod())
                .status(paymentDto.getStatus())
                .description(paymentDto.getDescription())
                .build();
        paymentService.save(payment);

        return ResponseEntity.created(new URI("/api/v0/subscription-payments/save")).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<SubscriptionPayment> paymentOptional = paymentService.findById(id);

        if (paymentOptional.isPresent()) {
            paymentService.deleteById(id);
            return ResponseEntity.ok("Payment deleted successfully");
        }
        return ResponseEntity.badRequest().build();
    }
}
