package com.bank.bankAccount.controller;

import com.bank.bankAccount.model.AccountBalance;
import com.bank.bankAccount.model.BankAccount;
import com.bank.bankAccount.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BankAccountService bankAccountService;

    private BankAccount bankAccount;
    private AccountBalance accountBalance;
    private List<BankAccount> bankAccountList;
    private List<AccountBalance> accountBalanceList;

    @BeforeEach
    void setUp() {
        bankAccountList= new ArrayList<>();
        accountBalanceList = new ArrayList<>();

        accountBalance = new AccountBalance();
        accountBalance.setCurrency("PLN");
        accountBalance.setBalance(BigDecimal.valueOf(2000));

        accountBalanceList.add(accountBalance);

        bankAccount = new BankAccount();
        bankAccount.setFirstName("Tom");
        bankAccount.setLastName("Scot");
        bankAccount.setPesel("96122357658");
        bankAccount.setAccountBalances(accountBalanceList);

        bankAccountList.add(bankAccount);
    }

    @Test
    void shouldGetAllBankAccounts() throws Exception {
        given(bankAccountService.getAllBankAccount()).willReturn(bankAccountList);

        mvc.perform(get("/api/bankAccounts")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pesel", is(bankAccount.getPesel())));
    }

    @Test
    void shouldGetBankAccountByPesel() throws Exception {
        String pesel = bankAccount.getPesel();
        given(bankAccountService.getBankAccountByPesel(pesel)).willReturn(bankAccount);

        mvc.perform(get("/api/bankAccounts/{pesel}")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("pesel", is(bankAccount.getPesel())));
    }

    @Test
    void ShouldAddBankAccount() throws Exception {
        when(bankAccountService.addBankAccount(any())).thenReturn(bankAccount);

        mvc.perform(post("/api/bankAccounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{ \"pesel\": \"99052907892\",\n" +
                "    \"firstName\": \"Patryk\",\n" +
                "    \"lastName\": \"Piecek\",\n" +
                "    \"accountBalances\": [\n" +
                "        {\n" +
                "            \"balance\": 2000,\n" +
                "            \"currency\": \"PLN\"\n" +
                "        }\n" +
                "    ]\n" +
                "}")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("pesel", is(bankAccount.getPesel())))
                .andExpect(jsonPath("firstName", is(bankAccount.getFirstName())));

    }

    @Test
    void exchangeMoney() {
    }

    @Test
    void updateBankAccount() throws Exception {
        when(bankAccountService.updateBankAccount(any())).thenReturn(bankAccount);

        mvc.perform(put("/api/bankAccounts/{pesel}", bankAccount.getPesel())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content("{\n" +
                "    \"pesel\": \"99052907892\",\n" +
                "    \"firstName\": \"Patryk\",\n" +
                "    \"lastName\": \"Piecek\",\n" +
                "    \"accountBalances\": [\n" +
                "        {\n" +
                "            \"balance\": 2000,\n" +
                "            \"currency\": \"PLN\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("pesel", is(bankAccount.getPesel())))
                .andExpect(jsonPath("firstName", is(bankAccount.getFirstName())));
    }

    @Test
    void deleteBankAccount() throws Exception {
        mvc.perform(delete("/api/bankAccounts/{pesel}", bankAccount.getPesel()))
                .andExpect(status().isOk());
    }
}