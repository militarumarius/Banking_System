package org.poo.bank.accounts;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public abstract class Account {
    @JsonProperty("IBAN")
    private final String IBAN;
    private final String type;
    private  double balance = 0.0;
    private final String currency;
    private List<Card> cards = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    @JsonIgnore
    private double minAmount ;

    @JsonIgnore
    public double getMinAmount() {
        return minAmount;
    }

    protected Account(String IBAN, String type, String currency) {
        this.IBAN = IBAN;
        this.type = type;
        this.currency = currency;
    }
    // Copy constructor
    protected Account(Account account) {
        this.IBAN = account.IBAN;
        this.type = account.type;
        this.balance = account.balance;
        this.currency = account.currency;
        this.cards = new ArrayList<>(account.cards);
    }

    public String getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Card> getCards() {
        return cards;
    }

    @JsonIgnore
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @JsonIgnore
    public String getIBAN() {
        return IBAN;
    }

    public void payOnline(CommandInput commandInput, double exchangeRate){}

    public List<Transaction> copyTransaction(){
        return null;
    };
    public boolean validatePayment(CommandInput commandInput, double exchangeRate){
        return false;
    };

    }
