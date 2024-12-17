package org.poo.bank.accounts;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Commerciant;
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
    private double interestRate;
    @JsonIgnore
    private double minAmount = 0.0;

    @JsonIgnore
    public double getMinAmount() {
        return minAmount;
    }

    protected Account(String IBAN, String type, String currency) {
        this.IBAN = IBAN;
        this.type = type;
        this.currency = currency;
        this.interestRate = 0;
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

    public void payOnline(double amount, double exchangeRate){
        this.setBalance(this.getBalance() - amount * exchangeRate);
    }
    public List<Transaction> copyTransaction(List<Transaction> transactions){
        List<Transaction> copy = new ArrayList<>();
        for(Transaction transaction : transactions)
            copy.add(new Transaction(transaction));
        return copy;
    }
    public boolean validatePayment(double amount, double exchangeRate){
        return this.getBalance() >= amount * exchangeRate
                && this.getBalance() > this.getMinAmount();
    }
    @JsonIgnore
    public double getInterestRate() {
        return interestRate;
    }
    public List<Transaction> getTransactionsInInterval(int startTimestamp, int endTimestamp) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
    public List<Transaction> getSpendingTransaction(int startTimestamp, int endTimestamp) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp &&
                    transaction.getTimestamp() <= endTimestamp && transaction.getDescription().equals("Card payment")) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public List<Commerciant> getCommerciants(List<Transaction> transactions) {
        List<Commerciant> commerciants = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Commerciant commerciant = new Commerciant(transaction.getCommerciant(), (Double) transaction.getAmount());
                commerciants.add(commerciant);
        }
        return commerciants;
    }
}
