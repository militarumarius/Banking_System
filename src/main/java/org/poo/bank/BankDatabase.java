package org.poo.bank;

import org.poo.bank.accounts.Account;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.poo.utils.Utils.resetRandom;

public class BankDatabase {
    final private List<User> users;
    final private List<ExchangeRate> exchangeRates;
    final Map<String, User> userMap = new HashMap<>();

    public BankDatabase(ObjectInput input) {
        resetRandom();
        users =  new ArrayList<>();
        exchangeRates = new ArrayList<>();
        for(UserInput user : input.getUsers())
            users.add(new User(user));
        for(ExchangeInput rate : input.getExchangeRates())
            addExchangeRate(rate);
        createEmailMap();
    }

    public Map<String, User> getUserMap() {
        return userMap;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public List<User> copyUsers(){
        List<User> copyUsers= new ArrayList<>();

        for(User user : users)
            copyUsers.add(new User(user));
        return copyUsers;
    }
    public void createEmailMap(){
        for (User user : users) {
            userMap.put(user.getEmail(), user);
        }
    }

    public void addExchangeRate(ExchangeInput rate){
        this.exchangeRates.add(new ExchangeRate(rate.getRate(), rate.getFrom(), rate.getTo()));
        double newRate = 1 / rate.getRate();
        this.exchangeRates.add(new ExchangeRate(newRate, rate.getTo(),rate.getFrom()));
    }
    public double findExchangeRate(String from, String to, List<String> visited) {
        if (from.equals(to)) {
            return 1;
        }
        visited.add(from);
        for (ExchangeRate rate : exchangeRates) {
            if (rate.getFrom().equals(from) && !visited.contains(rate.getTo())) {
                double partialRate = findExchangeRate(rate.getTo(), to, visited);
                if (partialRate != -1) {
                    return rate.getRate() * partialRate;
                }
            }
        }
        visited.remove(from);
        return -1;
    }

    public Account findUser(String iban){
        for (User user: users) {
            Account account = user.findAccount(iban);
            if(account != null)
                return account;
        }
        return null;
    }
}
