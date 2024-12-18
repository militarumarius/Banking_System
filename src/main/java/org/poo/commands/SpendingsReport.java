package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.actionHandler.ErrorOutput;
import org.poo.actionHandler.PrintOutput;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Commerciant;
import org.poo.transaction.Transaction;

import java.util.Comparator;
import java.util.List;

public class SpendingsReport implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    private final ArrayNode output;
    public SpendingsReport(BankDatabase bank, CommandInput commandInput, ArrayNode output){
        this.bank = bank;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        Account account = bank.findUser(commandInput.getAccount());
        if(account == null){
            ErrorOutput errorOutput = new ErrorOutput("Account not found", commandInput.getTimestamp());
            ObjectNode node = errorOutput.toObjectNode();
            PrintOutput report = new PrintOutput("spendingsReport", node, commandInput.getTimestamp());
            report.printCommand(output);
            return;
        }
        if(account.getType().equals("savings")) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            objectNode.put("command", "spendingsReport");
            node.put("error", "This kind of report is not supported for a saving account");
            objectNode.putPOJO("output", node);
            objectNode.put("timestamp", commandInput.getTimestamp());
            output.addPOJO(objectNode);
            return;
        }
        List<Transaction> filteredTransactions = account.
                getSpendingTransaction(commandInput.getStartTimestamp(), commandInput.getEndTimestamp());
        List<Commerciant> commerciants = account.
                getCommerciants(filteredTransactions);
        commerciants.sort(Comparator.comparing(Commerciant::getCommerciant));
        PrintOutput spendingsReport = new PrintOutput("spendingsReport",
                account.createOutputSpendingTransactionObject(filteredTransactions, commerciants),
                commandInput.getTimestamp());
        spendingsReport.printCommand(output);
    }
}
