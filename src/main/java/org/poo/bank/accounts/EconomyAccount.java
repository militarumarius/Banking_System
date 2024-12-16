package org.poo.bank.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EconomyAccount extends Account{
    @JsonIgnore
    private double interestRate;
    public EconomyAccount(String IBAN, String type, String currency, double interestRate) {
        super(IBAN, type, currency);
        this.interestRate = interestRate;
    }

    @JsonIgnore
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public EconomyAccount(EconomyAccount account) {
        super(account);
        this.interestRate = account.getInterestRate();
    }
}
