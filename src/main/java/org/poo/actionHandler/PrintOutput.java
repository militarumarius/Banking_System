package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.transaction.Commerciant;
import org.poo.transaction.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrintOutput {
    private Object object;
    private String principalComand;
    private int timestamp;
    private User user;

    public PrintOutput(final String command, final Object object, final int timestamp) {
        this.principalComand = command;
        this.object = object;
        this.timestamp = timestamp;
    }
    public PrintOutput(final String command, final int timestamp, final User user) {
        this.principalComand = command;
        this.timestamp = timestamp;
        this.user = user;
    }

    public void printCommand(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", principalComand);
        objectNode.putPOJO("output", object);
        objectNode.put("timestamp", timestamp);
        output.addPOJO(objectNode);
    }

    public void printTransaction(final ArrayNode output){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", "printTransactions");
        List<Transaction> copyTransactions = new ArrayList<>();
        for (Account account : user.getAccounts())
            copyTransactions.addAll(account.getTransactions());
        copyTransactions.sort(Comparator.comparing(Transaction::getTimestamp));
        objectNode.putPOJO("output", copyTransactions);
        objectNode.put("timestamp", timestamp);
        output.addPOJO(objectNode);
    }

    public static ObjectNode createOutputTransactionObject(final List<Transaction> filteredTransactions,
                                                           final Account account){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("balance", account.getBalance());
        node.put("currency", account.getCurrency());
        node.put("IBAN", account.getIBAN());
        node.putPOJO("transactions", filteredTransactions);
        return node;
    }
    public static ObjectNode createOutputSpendingTransactionObject(final List<Transaction> filteredTransactions,
                                                            final List<Commerciant> commerciants,
                                                            final Account account){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.putPOJO("commerciants", commerciants);
        node.put("balance", account.getBalance());
        node.put("currency", account.getCurrency());
        node.put("IBAN", account.getIBAN());
        node.putPOJO("transactions", filteredTransactions);
        return node;
    }


}
