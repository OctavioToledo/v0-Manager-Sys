package com.demoV1Project.infrastructure.persistence;

import com.demoV1Project.domain.model.SubscriptionPayment;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPaymentDao {
    List<SubscriptionPayment> findAll();

    Optional<SubscriptionPayment> findById(Long id);

    void save(SubscriptionPayment subscriptionPayment);

    void deleteById(Long id);
}
