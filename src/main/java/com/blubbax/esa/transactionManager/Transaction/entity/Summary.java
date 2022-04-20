package com.blubbax.esa.transactionManager.Transaction.entity;

import java.util.Objects;

public class Summary {

    private double income;
    private double expenses;
    private double balance;

    public Summary() {
        this.income = 0.0;
        this.expenses = 0.0;
        this.balance = 0.0;
    }

    public void considerTransaction(Transaction transaction) {
        double amount = transaction.getAmount();
        if (amount > 0) {
            this.income += amount;
        } else {
            this.expenses += Math.abs(amount);
        }
        this.balance = this.income - this.expenses;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Summary summary = (Summary) o;
        return Double.compare(summary.income, income) == 0 && Double.compare(summary.expenses, expenses) == 0 && Double.compare(summary.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(income, expenses, balance);
    }

    @Override
    public String toString() {
        return "Summary{" +
                "income=" + income +
                ", expenses=" + expenses +
                ", balance=" + balance +
                '}';
    }
}
