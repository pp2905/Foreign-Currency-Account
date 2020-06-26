package com.bank.bankAccount.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;


@Entity
@Table(name = "account_balances")
public class AccountBalance {


    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    private BigDecimal balance;

    @NotNull
    @NotEmpty
    @NotBlank
    private String currency;

    public AccountBalance(BigDecimal balance, String currency) {
        this.balance = balance;
        this.currency = currency;
    }

    public AccountBalance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "id=" + id +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
