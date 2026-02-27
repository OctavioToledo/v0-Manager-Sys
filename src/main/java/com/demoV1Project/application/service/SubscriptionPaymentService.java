package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.SubscriptionPayment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionPaymentService {

    List<SubscriptionPayment> findAll();

    Page<SubscriptionPayment> findAll(Pageable pageable);

    Optional<SubscriptionPayment> findById(Long id);

    void save(SubscriptionPayment subscriptionPayment);

    void deleteById(Long id);
}
