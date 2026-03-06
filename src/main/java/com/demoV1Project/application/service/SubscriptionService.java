package com.demoV1Project.application.service;

import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionDto;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionResponseDto;
import com.demoV1Project.util.enums.SubscriptionTier;

public interface SubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionTier tier);

    SubscriptionDto getMySubscription();
}
