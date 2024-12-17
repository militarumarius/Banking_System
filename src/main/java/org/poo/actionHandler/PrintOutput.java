package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;

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
    /**
     * method that make an JSON object for getPlayerIdx command
     * @param output array node to display the object
     */
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
        for (Account account : user.getAccounts())
            objectNode.putPOJO("output", account.copyTransaction(account.getTransactions()));
        objectNode.put("timestamp", timestamp);
        output.addPOJO(objectNode);
    }

}
