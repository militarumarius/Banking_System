package org.poo.bank.accounts;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;

import java.util.ArrayList;
import java.util.List;

@Setter
public abstract class Account {
    @JsonProperty("IBAN")
    private final String IBAN;
    private final String type;
    private  double balance = 0.0;
    private final String currency;
    private List<Card> cards = new ArrayList<>();

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
    public String getIBAN() {
        return IBAN;
    }
}
