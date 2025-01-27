package com.demoV1Project.infrastructure.persistence.impl;

import com.demoV1Project.domain.model.SubscriptionPayment;
import com.demoV1Project.infrastructure.persistence.SubscriptionPaymentDao;
import com.demoV1Project.domain.repository.SubscriptionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class SubscriptionPaymentDaoImpl implements SubscriptionPaymentDao {

    @Autowired
    private final SubscriptionPaymentRepository paymentRepository;

    @Override
    public List<SubscriptionPayment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<SubscriptionPayment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public void save(SubscriptionPayment subscriptionPayment) {
        paymentRepository.save(subscriptionPayment);
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}
