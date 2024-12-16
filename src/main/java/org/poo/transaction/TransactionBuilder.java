package org.poo.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransactionBuilder {
    private int timestamp;
    private String description;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;
    public TransactionBuilder(int timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }
    public TransactionBuilder senderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
        return this;
    }
    public TransactionBuilder receiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
        return this;
    }
    public TransactionBuilder amount(String amount){
        this.amount = amount;
        return this;
    }
    public TransactionBuilder transferType(String transferType){
        this.transferType = transferType;
        return this;
    }
    public Transaction build(){
        return new Transaction(this);
    }
}
