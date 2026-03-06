package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Subscription;
import com.demoV1Project.util.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    Optional<Subscription> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
