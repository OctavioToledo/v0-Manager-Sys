package com.demoV1Project.persistence.impl;

import com.demoV1Project.model.Transaction;
import com.demoV1Project.persistence.TransactionDao;
import com.demoV1Project.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionDaoImpl implements TransactionDao {

    @Autowired
    private final TransactionRepository transactionRepository;

    public TransactionDaoImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }


    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
