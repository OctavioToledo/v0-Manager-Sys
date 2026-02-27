package com.demoV1Project.application.service.Impl;

import com.demoV1Project.domain.model.SubscriptionPayment;
import com.demoV1Project.domain.repository.SubscriptionPaymentRepository;
import com.demoV1Project.application.service.SubscriptionPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    private final SubscriptionPaymentRepository subscriptionPaymentRepository;

    @Override
    public List<SubscriptionPayment> findAll() {
        return subscriptionPaymentRepository.findAll();
    }

    @Override
    public org.springframework.data.domain.Page<SubscriptionPayment> findAll(
            org.springframework.data.domain.Pageable pageable) {
        return subscriptionPaymentRepository.findAll(pageable);
    }

    @Override
    public Optional<SubscriptionPayment> findById(Long id) {
        return subscriptionPaymentRepository.findById(id);
    }

    @Override
    public void save(SubscriptionPayment subscriptionPayment) {
        subscriptionPaymentRepository.save(subscriptionPayment);
    }

    @Override
    public void deleteById(Long id) {
        subscriptionPaymentRepository.deleteById(id);
    }
}
