package com.demoV1Project.service.Impl;

import com.demoV1Project.model.SubscriptionPayment;
import com.demoV1Project.persistence.SubscriptionPaymentDao;
import com.demoV1Project.service.SubscriptionPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPaymentServiceImpl implements SubscriptionPaymentService {

    @Autowired
    private final SubscriptionPaymentDao paymentDao;

    @Override
    public List<SubscriptionPayment> findAll() {
        return paymentDao.findAll();
    }

    @Override
    public Optional<SubscriptionPayment> findById(Long id) {
        return paymentDao.findById(id);
    }

    @Override
    public void save(SubscriptionPayment subscriptionPayment) {
        paymentDao.save(subscriptionPayment);
    }

    @Override
    public void deleteById(Long id) {
        paymentDao.deleteById(id);
    }
}
