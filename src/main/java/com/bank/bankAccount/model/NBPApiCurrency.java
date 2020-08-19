package com.bank.bankAccount.model;

import java.util.List;

public class NBPApiCurrency {
    private String table;
    private String currency;
    private String code;
    private List<NBPApiRates> rates;

    public NBPApiCurrency(String table, String currency, String code, List<NBPApiRates> rates) {
        this.table = table;
        this.currency = currency;
        this.code = code;
        this.rates = rates;
    }

    public NBPApiCurrency() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<NBPApiRates> getRates() {
        return rates;
    }

    public void setRates(List<NBPApiRates> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "NBPApiCurrency{" +
                "table='" + table + '\'' +
                ", currency='" + currency + '\'' +
                ", code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}
