package com.demoV1Project.infrastructure.controllers;

import com.demoV1Project.application.service.SubscriptionService;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionCreateDto;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionDto;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/select")
    public ResponseEntity<SubscriptionResponseDto> selectPlan(@RequestBody SubscriptionCreateDto request) {
        SubscriptionResponseDto created = subscriptionService.createSubscription(request.getTier());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/my")
    public ResponseEntity<SubscriptionDto> getMySubscription() {
        SubscriptionDto subscription = subscriptionService.getMySubscription();
        if (subscription == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscription);
    }
}
