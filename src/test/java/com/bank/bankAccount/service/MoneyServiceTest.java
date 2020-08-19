package com.bank.bankAccount.service;

import com.bank.bankAccount.model.AccountBalance;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.repository.AccountBalanceRepository;
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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class MoneyServiceTest {

    @Mock
    private AccountBalanceRepository accountBalanceRepository;

    @InjectMocks
    private MoneyService moneyService;

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
    void shouldAddMoneyToAccount() {
        AccountBalance accountBalance = bankAccount.getAccountBalances().get(0);

        given(accountBalanceRepository.findById(accountBalance.getId())).willReturn(Optional.of(accountBalance));

        moneyService.addMoneyToAccount(accountBalance, BigDecimal.valueOf(300));

        AccountBalance expectedAccountBalance = accountBalanceRepository.findById(accountBalance.getId()).get();

        assertThat(expectedAccountBalance).isNotNull();
        assertThat(expectedAccountBalance).isEqualToComparingFieldByField(accountBalance);
    }

    @Test
    void shouldSubtractMoneyFromAccount() {
        AccountBalance accountBalance = bankAccount.getAccountBalances().get(0);

        given(accountBalanceRepository.findById(accountBalance.getId())).willReturn(Optional.of(accountBalance));

        moneyService.subtractMoneyFromAccount(accountBalance, BigDecimal.valueOf(500));

        AccountBalance expectedAccountBalance = accountBalanceRepository.findById(accountBalance.getId()).get();

        assertThat(expectedAccountBalance).isNotNull();
        assertThat(expectedAccountBalance).isEqualToComparingFieldByField(accountBalance);
    }
}