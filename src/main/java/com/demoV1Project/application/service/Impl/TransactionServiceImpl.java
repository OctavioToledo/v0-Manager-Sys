package com.demoV1Project.application.service.Impl;

import com.demoV1Project.domain.model.Transaction;
import com.demoV1Project.domain.repository.TransactionRepository;
import com.demoV1Project.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public org.springframework.data.domain.Page<Transaction> findAll(
            org.springframework.data.domain.Pageable pageable) {
        return transactionRepository.findAll(pageable);
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
