package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PrintCommands {
    private Object object;
    private String principalComand;
    private int timestamp;
    private String error;

    public PrintCommands(final String command, final Object object, final int timestamp) {
        this.principalComand = command;
        this.object = object;
        this.timestamp = timestamp;
    }
    /**
     * method that make an JSON object for getPlayerIdx command
     * @param output array node to display the object
     */
    public void printUsers(final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", principalComand);
        objectNode.putPOJO("output", object);
        objectNode.put("timestamp", timestamp);
        output.addPOJO(objectNode);
    }
    public void cardNotFound(final ArrayNode output){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("command", principalComand);
        objectNode.putPOJO("output", object);
        objectNode.put("timestamp", timestamp);
        output.addPOJO(objectNode);
    }

}
