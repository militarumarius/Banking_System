package org.poo.bank.accounts;

public class EconomyAccount extends Account{
    public EconomyAccount(String IBAN, String type, String currency, double interestRate) {
        super(IBAN, type, currency);
        this.setInterestRate(interestRate);
    }

    public EconomyAccount(EconomyAccount account) {
        super(account);
        this.setInterestRate(account.getInterestRate());
    }

}
