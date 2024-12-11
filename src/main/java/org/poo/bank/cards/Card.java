package org.poo.bank.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Card {
    private final String cardNumber;
    private final String status;
    @JsonIgnore
    private final int type;
    public Card(String cardNumber, int type) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.type = type;
    }
}
