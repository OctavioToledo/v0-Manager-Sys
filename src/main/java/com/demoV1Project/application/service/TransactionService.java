package com.demoV1Project.application.service;



import com.demoV1Project.domain.model.Transaction;


import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> findAll();
    Optional<Transaction> findById(Long id);
    void save(Transaction transaction);
    // void update(Transaction transaction);
    void deleteById(Long id);


}
