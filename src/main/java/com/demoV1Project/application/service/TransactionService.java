package com.demoV1Project.application.service;

import com.demoV1Project.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    List<Transaction> findAll();

    Page<Transaction> findAll(Pageable pageable);

    Optional<Transaction> findById(Long id);

    void save(Transaction transaction);

    // void update(Transaction transaction);
    void deleteById(Long id);

}
