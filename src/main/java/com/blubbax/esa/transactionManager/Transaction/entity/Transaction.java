package com.blubbax.esa.transactionManager.Transaction.entity;

import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Transaction {

    private @Id @GeneratedValue Long id;

    @NotEmpty
    private String userId;

    private Date date;

    @NotNull
    private double amount;

    private String description;



    public Transaction() {}

    public Transaction(String userId, Date date, double amount, String description) {
        this.userId = userId;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && id.equals(that.id) && userId.equals(that.userId) && Objects.equals(date, that.date) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date, amount, description);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", epoch=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }


}
