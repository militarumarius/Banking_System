package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.DebitCard;
import org.poo.bank.cards.OneTImeUseCard;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transaction.Transaction;
import org.poo.transaction.TransactionBuilder;


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
                    Commands printUsers = new Commands("printUsers", bank.copyUsers(), commandInput.getTimestamp());
                    printUsers.printUsers(output);
                }
                case "addAccount" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    Account account = FactoryAccount.createAccount(commandInput);
                    user.getAccounts().add(account);
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "New account created")
                            .build();
                    account.getTransactions().add(transaction);
                }
                case "createCard" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    Account account = user.findAccount(commandInput.getAccount());
                    Card newCard = new DebitCard(generateCardNumber(), 1, account);
                    user.addCard(account,newCard );
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "New card created")
                            .account(account.getIBAN())
                            .cardHolder(user.getEmail())
                            .card(newCard.getCardNumber())
                            .build();
                    account.getTransactions().add(transaction);
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
                        Card newCard = new OneTImeUseCard(generateCardNumber(), 1, account);
                        user.addCard(account, newCard);
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "New card created")
                                .account(account.getIBAN())
                                .cardHolder(user.getEmail())
                                .card(newCard.getCardNumber())
                                .build();
                        account.getTransactions().add(transaction);
                    }
                }
                case "deleteCard" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if (user == null)
                        break;
                    Account account = user.removeCard(commandInput.getCardNumber());
                    if(account ==  null)
                        break;
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "The card has been destroyed")
                            .account(account.getIBAN())
                            .cardHolder(user.getEmail())
                            .card(commandInput.getCardNumber())
                            .build();
                    account.getTransactions().add(transaction);
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
                    } else {
                        node.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
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
                        Card card = user.findCard(commandInput.getCardNumber());
                        if(card == null)
                            break;
                        if(card.getStatus().equals("frozen")){
                            Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "The card is frozen")
                                    .build();
                            card.getAccount().getTransactions().add(transaction);
                            break;
                        }
                        List <String> visited = new ArrayList<>();
                        double exchangeRate = bank.findExchangeRate(commandInput.getCurrency(), account.getCurrency(), visited);
                        if (exchangeRate > 0 && account.validatePayment(commandInput, exchangeRate)) {
                            account.payOnline(commandInput, exchangeRate);
                            Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "Card payment")
                                    .amount(exchangeRate * commandInput.getAmount())
                                    .commerciant(commandInput.getCommerciant())
                                    .build();
                            account.getTransactions().add(transaction);
                        } else if(!account.validatePayment(commandInput, exchangeRate)){
                            Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "Insufficient funds")
                                    .build();
                            account.getTransactions().add(transaction);
                        }
                    }
                }
                case "sendMoney"->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    Account receiver = bank.findUser(commandInput.getReceiver());
                    if(receiver == null)
                        break;
                    Account sender = user.findAccount(commandInput.getAccount());
                    if(sender == null)
                        break;
                    if(!sender.getIBAN().startsWith("RO"))
                        break;
                    List <String> visited = new ArrayList<>();
                    double exchangeRate = bank.findExchangeRate(sender.getCurrency(), receiver.getCurrency(), visited);
                    visited.clear();
                    if(sender.getBalance() < commandInput.getAmount()){
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "Insufficient funds")
                                .build();
                        sender.getTransactions().add(transaction);
                        break;
                    }
                    if (exchangeRate > 0 && sender.getBalance() > commandInput.getAmount()) {
                        sender.setBalance(sender.getBalance() - commandInput.getAmount());
                        receiver.setBalance(receiver.getBalance() + exchangeRate * commandInput.getAmount());
                        String amount = String.valueOf(commandInput.getAmount()) + " " + sender.getCurrency();
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), commandInput.getDescription())
                                .senderIBAN(commandInput.getAccount())
                                .receiverIBAN(commandInput.getReceiver())
                                .amount(amount)
                                .transferType("sent")
                                .build();
                        sender.getTransactions().add(transaction);
                    }
                }
                case "printTransactions" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    objectNode.put("command", "printTransactions");
                    for (Account account : user.getAccounts())
                        objectNode.putPOJO("output", account.copyTransaction());
                    objectNode.put("timestamp", commandInput.getTimestamp());
                    output.addPOJO(objectNode);

                }
                case "setAlias" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    Account account = user.findAccount(commandInput.getAccount());
                    bank.getAliasMap().put(commandInput.getAlias(), account);
                }
                case "checkCardStatus"->{
                    Card card = bank.findCard(commandInput.getCardNumber());
                    if(card == null){
                        ObjectNode node = JsonNodeFactory.instance.objectNode();
                        objectNode.put("command", "checkCardStatus");
                        node.put("timestamp", commandInput.getTimestamp());
                        node.put("description", "Card not found");
                        objectNode.putPOJO("output", node);
                        objectNode.put("timestamp", commandInput.getTimestamp());
                        output.addPOJO(objectNode);
                        break;
                    }
                    if(card.getAccount().getMinAmount() >= card.getAccount().getBalance() && card.getStatus().equals("active")){
                        card.setStatus("frozen");
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), "You have reached the minimum amount of funds, the card will be frozen")
                                .build();
                        card.getAccount().getTransactions().add(transaction);
                    }
                }
                case "setMinimumBalance"->{
                    Account account = bank.findUser(commandInput.getAccount());
                    account.setMinAmount(commandInput.getMinBalance());
                }
                case "changeInterestRate"->{
                    Account account = bank.findUser(commandInput.getAccount());
                    account.setInterestRate(commandInput.getInterestRate());
                }
                case "splitPayment"->{

                }
            }
        }
    }
}

