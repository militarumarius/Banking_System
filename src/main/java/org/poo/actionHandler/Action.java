package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.cards.OneTImeUseCard;
import org.poo.commands.*;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.transaction.Transaction;
import org.poo.transaction.TransactionBuilder;
import org.poo.transaction.TransactionDescription;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;
import static org.poo.utils.Utils.generateCardNumber;

public class Action {
    public static void actionHandler(final ObjectInput input, final ArrayNode output, BankDatabase bank) {
        for (CommandInput commandInput : input.getCommands()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            switch (commandInput.getCommand()) {
                case "printUsers" -> {
                    PrintUsers command = new PrintUsers(bank, commandInput, output);
                    command.execute();
                }
                case "addAccount" -> {
                    AddAccount command = new AddAccount(bank, commandInput, output);
                    command.execute();
                }
                case "createCard" ->{
                    CreateCard command = new CreateCard(bank,commandInput, output);
                    command.execute();
                }
                case "addFunds" ->{
                    AddFunds command = new AddFunds(bank, commandInput, output);
                    command.execute();
                }
                case "createOneTimeCard" ->{
                    CreateOneTimeCard command = new CreateOneTimeCard(bank, commandInput, output, commandInput.getAccount());
                    command.execute();
                }
                case "deleteCard" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if (user == null)
                        break;
                    Account account = user.removeCard(commandInput.getCardNumber());
                    if(account ==  null)
                        break;
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.CARD_DESTROYED.getMessage())
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
                        Account account = bank.findUser(commandInput.getAccount());
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.INVALID_DELETE_ACCOUNT.getMessage())
                                .build();
                        account.getTransactions().add(transaction);
                    }
                    objectNode.putPOJO("output", node);
                    objectNode.put("timestamp", commandInput.getTimestamp());
                    output.addPOJO(objectNode);

                }
                case "payOnline" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    if(user == null)
                        break;
                    Account account = user.getCardAccountMap().get(commandInput.getCardNumber());
                    if(account == null){
                        ErrorOutput errorOutput = new ErrorOutput("Card not found", commandInput.getTimestamp());
                        ObjectNode node = errorOutput.toObjectNode();
                        PrintOutput payOnline = new PrintOutput("payOnline", node, commandInput.getTimestamp());
                        payOnline.printCommand(output);
                        break;
                        }
                    Card card = user.findCard(commandInput.getCardNumber());
                    if(card == null)
                        break;
                    if(card.getStatus().equals("frozen")){
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.CARD_FROZEN.getMessage())
                                .build();
                        card.getAccount().getTransactions().add(transaction);
                        break;
                    }
                    List <String> visited = new ArrayList<>();
                    double exchangeRate = bank.findExchangeRate(commandInput.getCurrency(), account.getCurrency(), visited);
                    if(!account.validatePayment(commandInput.getAmount(), exchangeRate)){
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.INSUFFICIENT_FUNDS.getMessage())
                                .build();
                        account.getTransactions().add(transaction);
                        break;
                    }
                    if (exchangeRate <= 0)
                        break;
                    account.setBalance(account.getBalance() - commandInput.getAmount() * exchangeRate);
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.CARD_PAYMENT.getMessage())
                            .amount(exchangeRate * commandInput.getAmount())
                            .commerciant(commandInput.getCommerciant())
                            .build();
                    account.getTransactions().add(transaction);
                    if(card.getType() == 1) {
                        Transaction transaction1 = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.CARD_DESTROYED.getMessage())
                                .account(account.getIBAN())
                                .cardHolder(user.getEmail())
                                .card(card.getCardNumber())
                                .build();
                        account.getTransactions().add(transaction1);
                        account.getCards().remove(card);
                        user.getCardAccountMap().remove(card.getCardNumber());
                        CreateOneTimeCard command = new CreateOneTimeCard(bank, commandInput, output, account.getIBAN());
                        command.execute();
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
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.INSUFFICIENT_FUNDS.getMessage())
                                .build();
                        sender.getTransactions().add(transaction);
                        break;
                    }
                    if (exchangeRate <= 0)
                        break;
                    sender.setBalance(sender.getBalance() - commandInput.getAmount());
                    receiver.setBalance(receiver.getBalance() + exchangeRate * commandInput.getAmount());
                    String amountSender = String.valueOf(commandInput.getAmount()) + " " + sender.getCurrency();
                    Transaction transactionSender = new TransactionBuilder(commandInput.getTimestamp(), commandInput.getDescription())
                            .senderIBAN(commandInput.getAccount())
                            .receiverIBAN(commandInput.getReceiver())
                            .amount(amountSender)
                            .transferType("sent")
                            .build();
                    sender.getTransactions().add(transactionSender);
                    String amountReceiver = String.valueOf(exchangeRate * commandInput.getAmount()) + " " + receiver.getCurrency();
                    Transaction transactionReceiver = new TransactionBuilder(commandInput.getTimestamp(), commandInput.getDescription())
                            .senderIBAN(commandInput.getAccount())
                            .receiverIBAN(commandInput.getReceiver())
                            .amount(amountReceiver)
                            .transferType("received")
                            .build();
                    receiver.getTransactions().add(transactionReceiver);
                }
                case "printTransactions" ->{
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    PrintOutput printTransactions = new PrintOutput("printTransactions", commandInput.getTimestamp(), user);
                    printTransactions.printTransaction(output);
                }
                case "setAlias" -> {
                    User user = bank.getUserMap().get(commandInput.getEmail());
                    Account account = user.findAccount(commandInput.getAccount());
                    bank.getAliasMap().put(commandInput.getAlias(), account);
                }
                case "checkCardStatus"->{
                    Card card = bank.findCard(commandInput.getCardNumber());
                    if(card == null){
                        ErrorOutput errorOutput = new ErrorOutput("Card not found", commandInput.getTimestamp());
                        ObjectNode node = errorOutput.toObjectNode();
                        PrintOutput checkCardStatus = new PrintOutput("checkCardStatus", node, commandInput.getTimestamp());
                        checkCardStatus.printCommand(output);
                        break;
                    }
                    if(card.getAccount().getMinAmount() >= card.getAccount().getBalance() && card.getStatus().equals("active")){
                        card.setStatus("frozen");
                        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(),  TransactionDescription.MINIMUM_FUNDS_REACHED.getMessage())
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
                    if(!bank.checkSaving(account)){
                        objectNode.put("command", "changeInterestRate");
                        ErrorOutput errorOutput = new ErrorOutput("This is not a savings account", commandInput.getTimestamp());
                        ObjectNode node = errorOutput.toObjectNode();
                        PrintOutput changeInterestRate = new PrintOutput("changeInterestRate", node, commandInput.getTimestamp());
                        changeInterestRate.printCommand(output);
                    }
                    Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(),
                            TransactionDescription.INTEREST_RATE_CHANGE.getMessage()
                                    + commandInput.getInterestRate())
                            .build();
                    account.setInterestRate(commandInput.getInterestRate());
                    account.getTransactions().add(transaction);
                }
                case "addInterest" ->{
                    Account account = bank.findUser(commandInput.getAccount());
                    if(!bank.checkSaving(account)){
                        objectNode.put("command", "addInterest");
                        ErrorOutput errorOutput = new ErrorOutput("This is not a savings account", commandInput.getTimestamp());
                        ObjectNode node = errorOutput.toObjectNode();
                        PrintOutput addInterest = new PrintOutput("addInterest", node, commandInput.getTimestamp());
                        addInterest.printCommand(output);
                    }
                    double newSum = account.getBalance() * commandInput.getInterestRate();
                    account.setBalance(newSum);
                }
                case "splitPayment"->{
                    SplitPayment command = new SplitPayment(bank, commandInput, output);
                    command.execute();
                }
                case "report"->{
                   Report report = new Report(bank, commandInput, output);
                   report.execute();
                }
                case "spendingsReport"-> {
                    SpendingsReport command = new SpendingsReport(bank, commandInput, output);
                    command.execute();
                }
            }
        }
    }

}

