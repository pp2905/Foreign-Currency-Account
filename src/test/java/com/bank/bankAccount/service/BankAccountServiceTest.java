package com.bank.bankAccount.service;

import com.bank.bankAccount.model.AccountBalance;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    private BankAccount bankAccount;
    private List<BankAccount> bankAccountList;
    private AccountBalance accountBalance;
    private List<AccountBalance> accountBalanceList;

    @BeforeEach
    void setUp() {
        bankAccountList = new ArrayList<>();
        accountBalanceList = new ArrayList<>();

        accountBalance = new AccountBalance();
        accountBalance.setBalance(BigDecimal.valueOf(1000));
        accountBalance.setCurrency("PLN");

        accountBalanceList.add(accountBalance);

        bankAccount = new BankAccount();
        bankAccount.setFirstName("Tom");
        bankAccount.setLastName("Scott");
        bankAccount.setPesel("96122357658");
        bankAccount.setAccountBalances(accountBalanceList);

        bankAccountList.add(bankAccount);
    }

    @Test
    void shouldGetAllBankAccount() {
        given(bankAccountRepository.findAll()).willReturn(bankAccountList);

        List<BankAccount> expectedBankAccounts = bankAccountService.getAllBankAccount();

        assertThat(expectedBankAccounts).isNotEmpty();
        assertThat(expectedBankAccounts).hasSize(1);
        assertThat(expectedBankAccounts.get(0)).isEqualToComparingFieldByField(bankAccount);
    }

    @Test
    void shouldGetBankAccountByPesel() {
        String pesel = bankAccount.getPesel();

        given(bankAccountRepository.findById(pesel)).willReturn(java.util.Optional.ofNullable(bankAccount));

        BankAccount expectedBankAccount = bankAccountService.getBankAccountByPesel(pesel);

        assertThat(expectedBankAccount).isNotNull();
        assertThat(expectedBankAccount).isEqualToComparingFieldByField(bankAccount);
    }

    @Test
    void shouldAddBankAccount() {
        given(bankAccountRepository.findById(bankAccount.getPesel())).willReturn(Optional.empty());
        given(bankAccountRepository.save(bankAccount)).willReturn(bankAccount);

        BankAccount expectedBankAccount = bankAccountService.addBankAccount(bankAccount);

        assertThat(expectedBankAccount).isNotNull();
        assertThat(expectedBankAccount).isEqualToComparingFieldByField(bankAccount);
    }

    @Test
    void shouldUpdateBankAccount() {
        String pesel = bankAccount.getPesel();

        given(bankAccountRepository.findById(pesel)).willReturn(Optional.ofNullable(bankAccount));
        given(bankAccountRepository.save(bankAccount)).willReturn(bankAccount);

        bankAccount.setFirstName("Jeff");
        BankAccount expectedBankAccount = bankAccountService.updateBankAccount(bankAccount);

        assertThat(expectedBankAccount).isNotNull();
        assertThat(expectedBankAccount).isEqualToComparingFieldByField(bankAccount);
    }

    @Test
    void shouldDeleteBankAccount() {
        String pesel = bankAccount.getPesel();

        given(bankAccountRepository.findById(pesel)).willReturn(Optional.ofNullable(bankAccount));
        bankAccountService.deleteBankAccount(pesel);

        verify(bankAccountRepository).delete(bankAccount);
    }

    @Test
    void isPeselTaken() {
        String pesel = bankAccount.getPesel();

        given(bankAccountRepository.findById(pesel)).willReturn(Optional.empty());

        boolean response = bankAccountService.isPeselTaken(pesel);

        assertThat(response).isFalse();
    }

    @Test
    void isPeselValid() {
        String pesel = bankAccount.getPesel();

        boolean response = bankAccountService.isPeselValid(pesel);

        assertThat(response).isTrue();
    }

    @Test
    void isPeselOwnerOfAge() {
        String pesel = bankAccount.getPesel();

        boolean response = bankAccountService.isPeselOwnerOfAge(pesel);

        assertThat(response).isTrue();
    }
}