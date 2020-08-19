package com.bank.bankAccount.model;

import java.time.LocalDate;

public class NBPApiRates {

    private String no;
    private LocalDate effectiveDate;
    private Double mid;

    public NBPApiRates(String no, LocalDate effectiveDate, Double mid) {
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.mid = mid;
    }

    public NBPApiRates() {
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getMid() {
        return mid;
    }

    public void setMid(Double mid) {
        this.mid = mid;
    }
}
