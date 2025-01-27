package com.demoV1Project.application.service.Impl;

import com.demoV1Project.domain.model.Transaction;
import com.demoV1Project.infrastructure.persistence.TransactionDao;
import com.demoV1Project.application.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private final TransactionDao transactionDao;

    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }


    @Override
    public List<Transaction> findAll() {
        return transactionDao.findAll();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionDao.findById(id);
    }

    @Override
    public void save(Transaction transaction) {
        transactionDao.save(transaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionDao.deleteById(id);
    }
}
