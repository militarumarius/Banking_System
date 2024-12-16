package org.poo.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
public  class Transaction {
    private int timestamp;
    private String description;
    private String senderIBAN;
    private String receiverIBAN;
    private Object amount;
    private String transferType;
    private String cardHolder;
    private String account;
    private String card;
    private String commerciant;

    public Transaction(TransactionBuilder builder) {
        this.timestamp = builder.getTimestamp();
        this.amount = builder.getAmount();
        this.description = builder.getDescription();
        this.receiverIBAN = builder.getReceiverIBAN();
        this.senderIBAN = builder.getSenderIBAN();
        this.transferType = builder.getTransferType();
        this.account = builder.getAccount();
        this.cardHolder = builder.getCardHolder();
        this.card = builder.getCard();
        this.commerciant = builder.getCommerciant();
    }

    public Transaction(Transaction transaction){
        this.timestamp = transaction.getTimestamp();
        this.transferType = transaction.getTransferType();
        this.description = transaction.getDescription();
        this.receiverIBAN = transaction.getReceiverIBAN();
        this.senderIBAN = transaction.getSenderIBAN();
        this.amount = transaction.getAmount();
        this.cardHolder = transaction.getCardHolder();
        this.account = transaction.getAccount();
        this.card = transaction.getCard();
        this.commerciant = transaction.getCommerciant();
    }

}
