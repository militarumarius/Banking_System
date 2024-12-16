package org.poo.bank.accounts;

import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class BasicAccount extends Account{
    public BasicAccount(String IBAN, String type, String currency) {
        super(IBAN, type, currency);
    }
    public BasicAccount(BasicAccount account){
        super(account);
    }
    @Override
    public void payOnline(CommandInput commandInput, double exchangeRate){
            this.setBalance(this.getBalance() - commandInput.getAmount() * exchangeRate);
    }
    @Override
    public boolean validatePayment(CommandInput commandInput, double exchangeRate){
        return this.getBalance() >= commandInput.getAmount() * exchangeRate;
    }
    @Override
    public List<Transaction> copyTransaction(){
        List<Transaction> copy = new ArrayList<>();
        for(Transaction transaction : this.getTransactions())
            copy.add(new Transaction(transaction));
        return copy;
    }

}
