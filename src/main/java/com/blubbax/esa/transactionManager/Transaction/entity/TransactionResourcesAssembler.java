package com.blubbax.esa.transactionManager.Transaction.entity;

import com.blubbax.esa.transactionManager.Transaction.TransactionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TransactionResourcesAssembler implements RepresentationModelAssembler<Transaction, EntityModel<Transaction>> {

    @Override
    public EntityModel<Transaction> toModel(Transaction transaction) {

        return EntityModel.of(transaction,
                Link.of(linkTo(methodOn(TransactionController.class).getTransactionDataset(transaction.getId())).toString()
                        .replace(BasicLinkBuilder.linkToCurrentMapping().toString(), ""), "self"));
    }

}
