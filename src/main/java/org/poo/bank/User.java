package org.poo.bank;

import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public class User {
    final String firstName;
    final String lastName;
    final String email;
    final List<Account> accounts;
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

}
