package com.demoV1Project.application.service.Impl;

import com.demoV1Project.application.mapper.SubscriptionMapper;
import com.demoV1Project.application.service.SubscriptionService;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionDto;
import com.demoV1Project.domain.dto.SubscriptionDto.SubscriptionResponseDto;
import com.demoV1Project.domain.model.Subscription;
import com.demoV1Project.domain.model.User;
import com.demoV1Project.domain.repository.SubscriptionRepository;
import com.demoV1Project.domain.repository.UserRepository;
import com.demoV1Project.shared.security.TenantContext;
import com.demoV1Project.util.enums.Role;
import com.demoV1Project.util.enums.SubscriptionStatus;
import com.demoV1Project.util.enums.SubscriptionTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TenantContext tenantContext;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionResponseDto createSubscription(SubscriptionTier tier) {
        User currentUser = tenantContext.getCurrentUser();

        // Expire any existing active subscriptions for this user
        subscriptionRepository.findByUserIdAndStatus(currentUser.getId(), SubscriptionStatus.ACTIVE)
                .ifPresent(sub -> {
                    sub.setStatus(SubscriptionStatus.CANCELLED);
                    subscriptionRepository.save(sub);
                });

        Subscription newSubscription = Subscription.builder()
                .tier(tier)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDate.now())
                // Basic plans could be indefinite, Paid ones expire in 30 days.
                // Assuming indefinite for now as per requirements.
                .endDate(null)
                .user(currentUser)
                .build();

        Subscription saved = subscriptionRepository.save(newSubscription);

        // Upgrade role to ADMIN
        currentUser.setRole(Role.ADMIN);
        userRepository.save(currentUser);

        return SubscriptionResponseDto.builder()
                .subscription(subscriptionMapper.toDto(saved))
                .build();
    }

    @Override
    public SubscriptionDto getMySubscription() {
        User currentUser = tenantContext.getCurrentUser();
        Subscription activeSub = subscriptionRepository
                .findByUserIdAndStatus(currentUser.getId(), SubscriptionStatus.ACTIVE)
                .orElse(null);

        if (activeSub == null) {
            return null;
        }

        return subscriptionMapper.toDto(activeSub);
    }
}
