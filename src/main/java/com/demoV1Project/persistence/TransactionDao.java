package com.demoV1Project.persistence;

import com.demoV1Project.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDao {
    List<Transaction> findAll();
    Optional<Transaction> findById(Long id);
    void save(Transaction transaction);
   // void update(Transaction transaction);
    void deleteById(Long id);

}
