package org.poo.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {
    private int timestamp;
    private String description;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;

    public Transaction(TransactionBuilder builder) {
        this.timestamp = builder.getTimestamp();
        this.amount = builder.getAmount();
        this.description = builder.getDescription();
        this.receiverIBAN = builder.getReceiverIBAN();
        this.senderIBAN = builder.getSenderIBAN();
        this.transferType = builder.getTransferType();
    }
}
