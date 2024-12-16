package org.poo.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.DebitCard;
import org.poo.fileio.CommandInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.poo.utils.Utils.generateCardNumber;

public class User {
    final String firstName;
    final String lastName;
    final String email;
    final List<Account> accounts;
    @JsonIgnore
    private final Map<String, Account> cardAccountMap = new HashMap<>();
    @JsonIgnore
    public Map<String, Account> getCardAccountMap() {
        return cardAccountMap;
    }

    public User(UserInput user) {
        accounts = new ArrayList<>();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public User(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.accounts = new ArrayList<>();
        for (Account account: user.getAccounts()) {
            accounts.add(FactoryAccount.createCopyAccount(account));
        }
    }
    public Account findAccount(String iban) {
        for (Account account : accounts)
            if (account.getIBAN().equals(iban))
                return account;
        return null;
    }
    public void removeCard(String numberCard){
        for (Account account : accounts) {
            if (account.getType().equals("classic")) {
                account.getCards().removeIf(card -> card.getCardNumber().equals(numberCard));
                cardAccountMap.remove(numberCard);
                }
        }
    }

    public void addCard(Account account, Card card){
        if (account == null)
            return;
        account.getCards().add(card);
        cardAccountMap.put(card.getCardNumber(), account);
    }


}
