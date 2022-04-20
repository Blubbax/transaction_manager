package com.blubbax.esa.transactionManager.Transaction;

import com.blubbax.esa.transactionManager.Transaction.entity.Summary;
import com.blubbax.esa.transactionManager.Transaction.exception.TransactionNotFoundException;
import com.blubbax.esa.transactionManager.Transaction.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get current server status")
    @GetMapping("/api")
    public String getInfo() {
        return "Server is up and running";
    }

    @Operation(summary = "Get all transactions")
    @GetMapping("/api/transaction")
    public List<Transaction> getAllTransactionDatasets() {
        return transactionService.getAllTransactionDatasets();
    }

    @Operation(summary = "Get all transactions for a user")
    @GetMapping("/api/transaction/user/{userId}")
    public List<Transaction> getAllTransactionDatasetsByUser(@PathVariable String userId) {
        return transactionService.getAllTransactionDatasetsByUser(userId);
    }

    @Operation(summary = "Get transaction by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transaction",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "404", description = "No transaction found",
                    content = @Content)
    })
    @GetMapping("/api/transaction/{id}")
    public Transaction getTransactionDataset(@PathVariable Long id) {
        return transactionService.getTransactionDataset(id);
    }

    @Operation(summary = "Save new hydroponic log entry")
    @PostMapping("/api/transaction")
    public Transaction saveTransactionDataset(@RequestBody Transaction transaction) {
        return transactionService.saveTransactionDataset(transaction);
    }

    @Operation(summary = "Update transaction by its id")
    @PutMapping("/api/transaction/{id}")
    public Transaction updateTransactionDataset(@PathVariable Long id, @RequestBody Transaction newTransaction) {
        return transactionService.updateTransactionDataset(id, newTransaction);
    }

    @Operation(summary = "Delete transaction by its id")
    @DeleteMapping("/api/transaction/{id}")
    public void deleteTransactionDataset(@PathVariable Long id) {
        try {
            transactionService.deleteTransactionDataset(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new TransactionNotFoundException(id);
        }
    }

    @Operation(summary = "Get summary for user")
    @GetMapping("/api/summary/{userId}")
    public Summary getSummary(@PathVariable String userId) {
        return transactionService.calculateSummaryForUser(userId);
    }

}
