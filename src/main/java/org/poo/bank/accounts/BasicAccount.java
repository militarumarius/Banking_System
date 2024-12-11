package org.poo.bank.accounts;

public class BasicAccount extends Account{
    public BasicAccount(String IBAN, String type, String currency) {
        super(IBAN, type, currency);
    }
    public BasicAccount(BasicAccount account){
        super(account);
    }
}
