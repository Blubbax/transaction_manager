package com.blubbax.esa.transactionManager.Transaction;

import com.blubbax.esa.transactionManager.Transaction.entity.Summary;
import com.blubbax.esa.transactionManager.Transaction.exception.TransactionNotFoundException;
import com.blubbax.esa.transactionManager.Transaction.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get root system info")
    @GetMapping("/")
    public String getRoot() {
        return "Transaction Manager API";
    }

    @Operation(summary = "Get current server status")
    @GetMapping("/api")
    public String getInfo() {
        return "Server is up and running";
    }

    @Operation(summary = "Get all transactions")
    @GetMapping(value = "/api/transaction", produces = { "application/hal+json" })
    public List<Transaction> getAllTransactionDatasets() {

        List<Transaction> transactions = transactionService.getAllTransactionDatasets();
        for(final Transaction transaction: transactions) {
            transaction.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(transaction.getId())).withSelfRel());
        }

        return  transactions;
    }

    @Operation(summary = "Get all transactions for a user")
    @GetMapping(value = "/api/transaction/user/{userId}", produces = { "application/hal+json" }, params = { "page", "size" })
    public CollectionModel<Transaction> getAllTransactionDatasetsByUser(
            @PathVariable String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Transaction> transcation_page = transactionService.getAllTransactionDatasetsByUser(userId, page, size);

        if (page >= transcation_page.getTotalPages()) {
            throw new TransactionNotFoundException(null);
        }

        List<Transaction> transcations = transcation_page.toList();

        for(final Transaction transaction: transcations) {
            transaction.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(transaction.getId())).withSelfRel());
        }

        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(TransactionController.class).getAllTransactionDatasetsByUser(userId, 0, size)).withRel("first"));
        links.add(linkTo(methodOn(TransactionController.class).getAllTransactionDatasetsByUser(userId, transcation_page.getTotalPages(), size)).withRel("last"));
        if (page + 1 < transcation_page.getTotalPages())
            links.add(linkTo(methodOn(TransactionController.class).getAllTransactionDatasetsByUser(userId, page + 1, size)).withRel("next"));
        if (page > 0)
            links.add(linkTo(methodOn(TransactionController.class).getAllTransactionDatasetsByUser(userId, page - 1, size)).withRel("prev"));
        CollectionModel<Transaction> result = CollectionModel.of(transcations, links);

        return result;
    }

    @Operation(summary = "Get transaction by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the transaction",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "404", description = "No transaction found",
                    content = @Content)
    })
    @GetMapping(value = "/api/transaction/{id}", produces = { "application/hal+json" })
    public RepresentationModel<Transaction> getTransactionDataset(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionDataset(id);

        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(transaction.getId())).withSelfRel());
        RepresentationModel<Transaction> result = (RepresentationModel<Transaction>) RepresentationModel.of(transaction, links);
        return result;
    }

    @Operation(summary = "Save new transaction")
    @PostMapping("/api/transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully created",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Transaction data not valid",
                    content = @Content)
    })
    public Transaction saveTransactionDataset(@RequestBody @Valid Transaction transaction) {
        Transaction new_transaction = transactionService.saveTransactionDataset(transaction);
        new_transaction.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(new_transaction.getId())).withSelfRel());
        return new_transaction;
    }

    @Operation(summary = "Update transaction by its id")
    @PutMapping("/api/transaction/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully updated",
                    content = { @Content(mediaType = "application/hal+json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Transaction data not valid",
                    content = @Content)
    })
    public Transaction updateTransactionDataset(@PathVariable Long id, @RequestBody @Valid Transaction newTransaction) {
        Transaction updated_transaction = transactionService.updateTransactionDataset(id, newTransaction);
        updated_transaction.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(updated_transaction.getId())).withSelfRel());
        return updated_transaction;
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
