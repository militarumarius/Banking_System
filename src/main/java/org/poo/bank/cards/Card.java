package org.poo.bank.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;

@Getter @Setter
public abstract class Card {
    private final String cardNumber;
    private final String status;
    @JsonIgnore
    private final int type;
    @JsonIgnore
    private final Account account;
    public Card(String cardNumber, int type, Account account) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.type = type;
        this.account = account;
    }
}
