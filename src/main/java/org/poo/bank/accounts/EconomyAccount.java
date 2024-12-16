package org.poo.bank.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

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
