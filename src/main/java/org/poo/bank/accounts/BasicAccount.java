package org.poo.bank.accounts;

import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;

public class BasicAccount extends Account{
    public BasicAccount(String IBAN, String type, String currency) {
        super(IBAN, type, currency);
    }
    public BasicAccount(BasicAccount account){
        super(account);
    }
    @Override
    public void payOnline(CommandInput commandInput, double exchangeRate){
        if(this.getBalance() >= commandInput.getAmount() * exchangeRate)
            this.setBalance(this.getBalance() - commandInput.getAmount() * exchangeRate);
    }

}
