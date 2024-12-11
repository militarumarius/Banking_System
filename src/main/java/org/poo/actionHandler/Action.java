package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.bank.cards.DebitCard;
import org.poo.bank.cards.OneTImeUseCard;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

import java.util.ArrayList;
import java.util.List;

import static org.poo.utils.Utils.generateCardNumber;

public class Action {
    public static void actionHandler(final ObjectInput input, final ArrayNode output, BankDatabase bank) {
        for (CommandInput commandInput : input.getCommands()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            switch (commandInput.getCommand()) {
                case "printUsers" -> {
                    List<User> copyUsers= new ArrayList<>();

                    for(User user : bank.getUsers())
                        copyUsers.add(new User(user));
                    objectNode.put("command", "printUsers");
                    objectNode.putPOJO("output", copyUsers);
                    objectNode.put("timestamp", commandInput.getTimestamp());
                    output.addPOJO(objectNode);
                }
                case "addAccount" -> {
                    for(User user : bank.getUsers()) {
                        if(user.getEmail().equals(commandInput.getEmail()))
                            user.getAccounts().add(FactoryAccount.createAcoount(commandInput));
                    }
                }
                case "createCard" ->{
                    for(User user : bank.getUsers()) {
                        if(user.getEmail().equals(commandInput.getEmail())) {
                            for(Account account : user.getAccounts()){
                                    if(account.getIBAN().equals(commandInput.getAccount()))
                                        account.getCards().add(new DebitCard(generateCardNumber(), 1));
                            }
                        }

                    }
                }
                case "addFunds" ->{
                    for(User user : bank.getUsers()) {
                            for(Account account : user.getAccounts()){
                                if(account.getIBAN().equals(commandInput.getAccount()))
                                    account.setBalance(account.getBalance() + commandInput.getAmount());
                            }
                    }

                }
                case "createOneTimeCard" ->{
                    for(User user : bank.getUsers()) {
                        if(user.getEmail().equals(commandInput.getEmail())) {
                            for(Account account : user.getAccounts()){
                                if(account.getIBAN().equals(commandInput.getAccount()))
                                    account.getCards().add(new OneTImeUseCard(generateCardNumber(), 0));
                            }
                        }

                    }
                }
                case "deleteCard" ->{
                    for(User user : bank.getUsers()) {
                        if(user.getEmail().equals(commandInput.getEmail())) {
                            for(Account account : user.getAccounts()){
                                if(account.getType().equals("classic"))
                                    account.getCards().removeIf(card -> card.getCardNumber().equals(commandInput.getCardNumber()));

                            }
                        }

                    }
                }
                case "deleteAccount" -> {
                    boolean check = false;
                    ObjectNode node = JsonNodeFactory.instance.objectNode();
                    objectNode.put("command", "deleteAccount");
                    for(User user : bank.getUsers()) {
                        if(user.getEmail().equals(commandInput.getEmail()))
                            check = user.getAccounts().removeIf(account ->
                                    account.getIBAN().equals(commandInput.getAccount()) && account.getBalance() == 0);
                    }
                    if(check) {
                        node.put("success", "Account deleted");
                        node.put("timestamp", commandInput.getTimestamp());
                    }
                    objectNode.putPOJO("output", node);
                    objectNode.put("timestamp", commandInput.getTimestamp());
                    output.addPOJO(objectNode);
                }
            }
        }
    }
}

