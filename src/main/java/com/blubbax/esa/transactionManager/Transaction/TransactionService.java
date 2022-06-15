package com.blubbax.esa.transactionManager.Transaction;

import com.blubbax.esa.transactionManager.Transaction.entity.Summary;
import com.blubbax.esa.transactionManager.Transaction.entity.Transaction;
import com.blubbax.esa.transactionManager.Transaction.exception.TransactionNotFoundException;
import com.blubbax.esa.transactionManager.Transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactionDatasets() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionDataset(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> getAllTransactionDatasetsByUser(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Page<Transaction> getAllTransactionDatasetsByUser(String userId, int page, int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Transaction> pagedResult = transactionRepository.findByUserId(userId, paging);

        return pagedResult;
    }

    public Transaction saveTransactionDataset(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransactionDataset(@PathVariable Long id, @RequestBody Transaction newTransaction) {
        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setDate(newTransaction.getDate());
                    transaction.setAmount(newTransaction.getAmount());
                    transaction.setDescription(newTransaction.getDescription());
                    transaction.setUserId(newTransaction.getUserId());
                    return transactionRepository.save(transaction);
                }).orElseGet(() -> {
                    newTransaction.setId(id);
                    return transactionRepository.save(newTransaction);
                });
    }

    public void deleteTransactionDataset(@PathVariable Long id) throws EmptyResultDataAccessException {
        transactionRepository.deleteById(id);
    }

    public Summary calculateSummaryForUser(String userId) {
        List<Transaction> transactions = this.getAllTransactionDatasetsByUser(userId);
        Summary summary = new Summary();

        for (Transaction transaction : transactions) {
            summary.considerTransaction(transaction);
        }

        return summary;
    }

}
