package org.poo.bank.cards;

import org.poo.bank.accounts.Account;

public class DebitCard extends Card{
    public DebitCard(String cardNumber, String type, Account account) {
        super(cardNumber, type, account);
    }
}
