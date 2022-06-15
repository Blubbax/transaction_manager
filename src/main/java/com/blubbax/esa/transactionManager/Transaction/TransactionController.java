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
    @GetMapping(value = "/api/transaction/{id}", produces = { "application/hal+json" })
    public RepresentationModel<Transaction> getTransactionDataset(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionDataset(id);

        ArrayList<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(TransactionController.class).getTransactionDataset(transaction.getId())).withSelfRel());
        links.add(linkTo(methodOn(TransactionController.class).updateTransactionDataset(transaction.getId(), transaction)).withRel("update"));
        RepresentationModel<Transaction> result = (RepresentationModel<Transaction>) RepresentationModel.of(transaction, links);
        return result;
    }

    @Operation(summary = "Save new hydroponic log entry")
    @PostMapping("/api/transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Transaction data not valid",
                    content = @Content)
    })
    public Transaction saveTransactionDataset(@RequestBody @Valid Transaction transaction) {
        return transactionService.saveTransactionDataset(transaction);
    }

    @Operation(summary = "Update transaction by its id")
    @PutMapping("/api/transaction/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully updated",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Transaction data not valid",
                    content = @Content)
    })
    public Transaction updateTransactionDataset(@PathVariable Long id, @RequestBody @Valid Transaction newTransaction) {
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
