package org.poo.bank.accounts;

public class EconomyAccount extends Account{
    private final double interestRate;
    public EconomyAccount(String IBAN, String type, String currency, double interestRate) {
        super(IBAN, type, currency);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public EconomyAccount(EconomyAccount account) {
        super(account);
        this.interestRate = account.getInterestRate();
    }
}
