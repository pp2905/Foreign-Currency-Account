package com.bank.bankAccount.service;

import com.bank.bankAccount.exception.BadRequestException;
import com.bank.bankAccount.exception.NotAcceptableException;
import com.bank.bankAccount.exception.NotFoundException;
import com.bank.bankAccount.model.AccountBalance;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.repository.AccountBalanceRepository;
import com.bank.bankAccount.repository.BankAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccount> getAllBankAccount() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        if(bankAccounts.isEmpty()) {
            throw new NotFoundException("Not found any bank Accounts");
        }

        return bankAccounts;
    }

    public BankAccount getBankAccountByPesel(String pesel) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(pesel);

        return bankAccount.orElseThrow(() -> new NotFoundException(String.format("Not found Bank Account with pesel: %s", pesel)));
    }

    public BankAccount addBankAccount(BankAccount bankAccount) {
        String pesel = bankAccount.getPesel();

        if(isPeselTaken(pesel)) {
            throw new NotAcceptableException("Given pesel is already registered");
        }

        if(!isPeselValid(pesel)) {
            throw new NotAcceptableException(String.format("Pesel: %s is incorrect", pesel));
        }

        if(!isPeselOwnerOfAge(pesel)) {
            throw new NotAcceptableException("Pesel Owner is not of age");
        }

        if(bankAccount.getAccountBalances().size() > 1) {
            throw new NotAcceptableException("Should give only one account balance");
        }

        if(bankAccount.getAccountBalances().get(0).getCurrency() == null || !bankAccount.getAccountBalances().get(0).getCurrency().equals("PLN")) {
            throw new NotAcceptableException("Given currency should be PLN");
        }

        if(bankAccount.getAccountBalances().get(0).getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotAcceptableException("Balance amount should not be negative");
        }

        bankAccount.getAccountBalances().add(new AccountBalance(BigDecimal.ZERO, "USD"));

        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount updateBankAccount(BankAccount bankAccount) {
        //if not found getBankAccountByPesel throw NotFoundException exception
        BankAccount getBankAccount = getBankAccountByPesel(bankAccount.getPesel());

        return bankAccountRepository.save(bankAccount);
    }

    public void deleteBankAccount(String pesel) {
        //if not found getBankAccountByPesel throw NotFoundException exception
        BankAccount getBankAccount = getBankAccountByPesel(pesel);

        bankAccountRepository.delete(getBankAccount);
    }

    public boolean isPeselTaken(String pesel) {
        return bankAccountRepository.findById(pesel).isPresent();
    }

    public boolean isPeselValid(String pesel) {
        //https://obywatel.gov.pl/pl/dokumenty-i-dane-osobowe/czym-jest-numer-pesel
        if(pesel.length() != 11) {
            return false;
        }

        int sum = 0;
        int controlNumber = Integer.valueOf(pesel.substring(10, 11));

        List<Integer> digitWeight = new ArrayList<>();
        digitWeight.addAll(Arrays.asList(1, 3, 7, 9 ,1 ,3, 7 ,9, 1, 3));

        for(int i=0; i<digitWeight.size(); i++) {
            sum += Integer.valueOf(pesel.substring(i, i+1)) *digitWeight.get(i);
        }

        sum = sum % 10;
        int result = (10-sum) % 10;

        return result == controlNumber;
    }

    public boolean isPeselOwnerOfAge(String pesel) {
        //https://obywatel.gov.pl/pl/dokumenty-i-dane-osobowe/czym-jest-numer-pesel
        String stringYear = pesel.substring(0,2);
        String stringMonth = pesel.substring(2,4);
        String stringDay = pesel.substring(4,6);

        Long longYear = Long.parseLong(stringYear);
        Long longMonth = Long.parseLong(stringMonth);
        Long longDay = Long.parseLong(stringDay);

        String year = "";
        String month = "";
        String day = String.valueOf(longDay);

        if(longMonth >= 1 && longMonth <=12) {
            year = "19"+stringYear;
            month = stringMonth;
        } else if (longMonth >= 21 && longMonth <=32) {
            year = "20"+stringYear;
            month = String.valueOf(longMonth-20) ;
        } else {
            throw new NotAcceptableException(String.format("Pesel: %s is incorrect", pesel));
        }

        LocalDate birthDate = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));

        if(!LocalDate.now().minusYears(18).isBefore(birthDate)) {
            return true;
        }

        return false;
    }
}
