package com.bank.bankAccount.service;

import com.bank.bankAccount.exception.BadRequestException;
import com.bank.bankAccount.exception.NotAcceptableException;
import com.bank.bankAccount.model.AccountBalance;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.model.NBPApiCurrency;
import com.bank.bankAccount.repository.AccountBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MoneyService {

    private final AccountBalanceRepository accountBalanceRepository;
    private final BankAccountService bankAccountService;

    @Autowired
    public MoneyService(AccountBalanceRepository accountBalanceRepository, BankAccountService bankAccountService) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.bankAccountService = bankAccountService;
    }

    public BankAccount exchangeMoney(String pesel, BigDecimal amount, String currencyFrom, String currencyTo){
        //if not found getBankAccountByPesel throw NotFoundException exception
        BankAccount bankAccount = bankAccountService.getBankAccountByPesel(pesel);

        if(amount == null || currencyFrom.isEmpty() || currencyTo.isEmpty()) {
            throw new BadRequestException("Params: 'amount, from, to' should not be empty");
        }

        AccountBalance accountBalanceFrom = bankAccount.getAccountBalanceByCurrency(currencyFrom);
        AccountBalance accountBalanceTo = bankAccount.getAccountBalanceByCurrency(currencyTo);

        if(accountBalanceFrom == null) {
            throw new NotAcceptableException(String.format("You dont have account with currency: %s", currencyFrom));
        }

        if(accountBalanceTo == null) {
            throw new NotAcceptableException(String.format("You dont have account with currency: %s", currencyTo));
        }

        if(accountBalanceFrom.getBalance().compareTo(amount) < 0) {
            throw new NotAcceptableException("You dont have enough money");
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotAcceptableException("Amount to exchange should be greater than 0");
        }

        String currencyToCheck = currencyFrom.equals("PLN") ? currencyTo : currencyFrom;

        WebClient.Builder builder = WebClient.builder();
        NBPApiCurrency nbpApiCurrency = builder.build()
                .get()
                .uri("http://api.nbp.pl/api/exchangerates/rates/a/" + currencyToCheck)
                .retrieve()
                .bodyToMono(NBPApiCurrency.class)
                .block();


        BigDecimal exchangeRate = new BigDecimal(nbpApiCurrency.getRates().get(0).getMid());
        BigDecimal amountAfterExchange = currencyFrom.equals("PLN") ? amount.divide(exchangeRate, 2 , RoundingMode.HALF_UP) : amount.multiply(exchangeRate);

        subtractMoneyFromAccount(accountBalanceFrom, amount);
        addMoneyToAccount(accountBalanceTo, amountAfterExchange);

        return bankAccountService.getBankAccountByPesel(pesel);
    }

    public void addMoneyToAccount(AccountBalance accountBalance, BigDecimal amountToAdd) {
        accountBalance.setBalance(accountBalance.getBalance().add(amountToAdd));

        accountBalanceRepository.save(accountBalance);
    }

    public void subtractMoneyFromAccount(AccountBalance accountBalance, BigDecimal amountToSubtract) {
        accountBalance.setBalance(accountBalance.getBalance().subtract(amountToSubtract));

        accountBalanceRepository.save(accountBalance);
    }
}
