package com.bank.bankAccount.controller;

import com.bank.bankAccount.exception.NotAcceptableException;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.service.BankAccountService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bankAccounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountService.getAllBankAccount();
    }

    @GetMapping(
            path = "{bankAccountPesel}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BankAccount getBankAccountByPesel(@PathVariable("bankAccountPesel") String pesel) {
        return bankAccountService.getBankAccountByPesel(pesel);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BankAccount addBankAccount(@Valid @RequestBody BankAccount bankAccount, Errors errors) {
        if(errors.hasErrors()) {
            throw new NotAcceptableException(errors.getFieldError().getField()+" "+errors.getFieldError().getDefaultMessage());
        }

        return bankAccountService.addBankAccount(bankAccount);
    }

    @PostMapping(
            path = "/exchangeMoney/{bankAccountPesel}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BankAccount exchangeMoney(@PathVariable("bankAccountPesel") String pesel, @RequestBody JsonNode jsonNode) {
        BigDecimal amount = new BigDecimal(jsonNode.path("amount").asText());
        String currencyFrom = jsonNode.path("from").asText();
        String currencyTo = jsonNode.path("to").asText();

        return bankAccountService.exchangeMoney(pesel, amount, currencyFrom, currencyTo);
    }

    @PutMapping(
            path = "{bankAccountPesel}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public BankAccount updateBankAccount(@Valid @PathVariable("bankAccountPesel") String pesel, @RequestBody BankAccount bankAccount, Errors errors) {
        if(errors.hasErrors()) {
            throw new NotAcceptableException(errors.getFieldError().getField()+" "+errors.getFieldError().getDefaultMessage());
        }

        bankAccount.setPesel(pesel);
        return bankAccountService.updateBankAccount(bankAccount);
    }

    @DeleteMapping(
            path = "{bankAccountPesel}"
    )
    public void deleteBankAccount(@PathVariable("bankAccountPesel") String pesel) {
        bankAccountService.deleteBankAccount(pesel);
    }
}
