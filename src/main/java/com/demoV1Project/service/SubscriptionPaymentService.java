package com.demoV1Project.service;

import com.demoV1Project.model.SubscriptionPayment;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPaymentService {

    List<SubscriptionPayment> findAll();

    Optional<SubscriptionPayment> findById(Long id);

    void save(SubscriptionPayment subscriptionPayment);

    void deleteById(Long id);
}

