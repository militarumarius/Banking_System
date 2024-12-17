package org.poo.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionBuilder {
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
    private String currency;
    private String error;
    private List<String> involvedAccounts = new ArrayList<>();

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
    public TransactionBuilder amount(Object amount){
        this.amount = amount;
        return this;
    }
    public TransactionBuilder transferType(String transferType){
        this.transferType = transferType;
        return this;
    }
    public TransactionBuilder account(String account){
        this.account = account;
        return this;
    }
    public TransactionBuilder cardHolder(String cardHolder){
        this.cardHolder = cardHolder;
        return this;
    }
    public TransactionBuilder card(String card){
        this.card = card;
        return this;
    }
    public TransactionBuilder commerciant(String commerciant){
        this.commerciant = commerciant;
        return this;
    }
    public TransactionBuilder involvedAccounts(List<String> involvedAccounts){
        this.involvedAccounts = involvedAccounts;
        return this;
    }
    public TransactionBuilder currency(String currency){
        this.currency = currency;
        return this;
    }
    public TransactionBuilder error(String error){
        this.error = error;
        return this;
    }
    public Transaction build(){
        return new Transaction(this);
    }
}
