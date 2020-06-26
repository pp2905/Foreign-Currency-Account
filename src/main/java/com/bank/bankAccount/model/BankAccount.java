package com.bank.bankAccount.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @NotNull
    @NotEmpty
    @NotBlank
    private String pesel;

    @NotNull
    @NotEmpty
    @NotBlank
    private String firstName;

    @NotNull
    @NotEmpty
    @NotBlank
    private String lastName;

    @NotNull
    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<AccountBalance> accountBalances;

    public BankAccount() {
    }

    public String getPesel() {
        return pesel;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<AccountBalance> getAccountBalances() {
        return accountBalances;
    }

    public AccountBalance getAccountBalanceByCurrency(String currency) {
        AccountBalance accountBalance = null;

        for(AccountBalance balance : accountBalances) {
            if(balance.getCurrency().equals(currency)) {
                accountBalance = balance;
            }
        }

        return accountBalance;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAccountBalances(List<AccountBalance> accountBalances) {
        this.accountBalances = accountBalances;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "pesel='" + pesel + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accountBalances=" + accountBalances +
                '}';
    }
}
