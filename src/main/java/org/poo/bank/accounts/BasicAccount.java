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

}
