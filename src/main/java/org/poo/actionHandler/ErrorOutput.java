package org.poo.actionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorOutput {
    private final String description;
    private final int timestamp;
    public ErrorOutput(final String description, final int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

    public ObjectNode toObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("timestamp", this.timestamp);
        objectNode.put("description", this.description);
        return objectNode;
    }
}
