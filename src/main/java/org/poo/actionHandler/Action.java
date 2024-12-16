package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankDatabase;
import org.poo.bank.ExchangeRate;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.DebitCard;
import org.poo.bank.cards.OneTImeUseCard;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.poo.utils.Utils.generateCardNumber;

public class Action {
    public static void actionHandler(final ObjectInput input, final ArrayNode output, BankDatabase bank) {
        for (CommandInput commandInput : input.getCommands()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            switch (commandInput.getCommand()) {
                case "printUsers" -> {
                    Commands printUsers = new Commands("printUsers", bank.copyUsers(), commandInput.getTimestamp());
                    printUsers.printUsers(output);
                }
                case "addAccount" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    user.getAccounts().add(FactoryAccount.createAccount(commandInput));
                }
                case "createCard" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    Account account = user.findAccount(commandInput.getAccount());
                    user.addCard(account,new DebitCard(generateCardNumber(), 1, account) );
                }
                case "addFunds" ->{
                    for(User user : bank.getUsers()) {
                        Account account = user.findAccount(commandInput.getAccount());
                        if(account != null)
                            account.setBalance(account.getBalance() + commandInput.getAmount());
                    }
                }
                case "createOneTimeCard" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user != null) {
                        Account account = user.findAccount(commandInput.getAccount());
                        user.addCard(account,new OneTImeUseCard(generateCardNumber(), 1, account) );
                    }
                }
                case "deleteCard" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if (user == null)
                        break;
                    user.removeCard(commandInput.getCardNumber());
                }
                case "deleteAccount" -> {
                    boolean check = false;
                    ObjectNode node = JsonNodeFactory.instance.objectNode();
                    objectNode.put("command", "deleteAccount");
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user != null)
                        check = user.getAccounts().removeIf(account ->
                                account.getIBAN().equals(commandInput.getAccount()) && account.getBalance() == 0);
                    if(check) {
                        node.put("success", "Account deleted");
                        node.put("timestamp", commandInput.getTimestamp());
                    }
                    objectNode.putPOJO("output", node);
                    objectNode.put("timestamp", commandInput.getTimestamp());
                    output.addPOJO(objectNode);
                }
                case "payOnline" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user != null) {
                        Account account = user.getCardAccountMap().get(commandInput.getCardNumber());
                        if(account == null){
                            ObjectNode node = JsonNodeFactory.instance.objectNode();
                            objectNode.put("command", "payOnline");
                            node.put("timestamp", commandInput.getTimestamp());
                            node.put("description", "Card not found");
                            objectNode.putPOJO("output", node);
                            objectNode.put("timestamp", commandInput.getTimestamp());
                            output.addPOJO(objectNode);
                            break;
                        }
                        List <String> visited = new ArrayList<>();
                        double exchangeRate = bank.findExchangeRate(commandInput.getCurrency(), account.getCurrency(), visited);
                        if (exchangeRate > 0)
                            account.payOnline(commandInput,exchangeRate);
                    }
                }
                case "sendMoney"->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user != null) {
                        Account receiver = bank.findUser(commandInput.getReceiver());
                        if(receiver == null)
                            break;
                        Account sender = user.findAccount(commandInput.getAccount());
                        if(sender == null)
                            break;
                        if(sender.getBalance() < commandInput.getAmount())
                            break;
                        List <String> visited = new ArrayList<>();
                        double exchangeRate = bank.findExchangeRate(sender.getCurrency(), receiver.getCurrency(), visited);
                        visited.clear();
                        if (exchangeRate > 0 && sender.getBalance() > commandInput.getAmount()) {
                            sender.setBalance(sender.getBalance() - commandInput.getAmount());
                            receiver.setBalance(receiver.getBalance() + exchangeRate * commandInput.getAmount());
                        }
                    }
                }
            }
        }
    }
}

