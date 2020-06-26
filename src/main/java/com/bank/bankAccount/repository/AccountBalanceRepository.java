package com.bank.bankAccount.repository;

import com.bank.bankAccount.model.AccountBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBalanceRepository extends JpaRepository <AccountBalance, Integer> {
}
