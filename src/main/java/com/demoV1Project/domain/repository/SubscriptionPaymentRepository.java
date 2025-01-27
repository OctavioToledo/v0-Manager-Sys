package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.SubscriptionPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, Long> {
}
