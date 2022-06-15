package com.blubbax.esa.transactionManager.Transaction.repository;

import com.blubbax.esa.transactionManager.Transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(String userId);
    Page<Transaction> findByUserId(String userId, Pageable pageable);
}
