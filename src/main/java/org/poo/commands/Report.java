package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.actionHandler.ErrorOutput;
import org.poo.actionHandler.PrintOutput;
import org.poo.bank.BankDatabase;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Commerciant;
import org.poo.transaction.Transaction;

import java.util.Comparator;
import java.util.List;

public class Report implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    private final ArrayNode output;
    public Report(BankDatabase bank, CommandInput commandInput, ArrayNode output){
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
            PrintOutput report = new PrintOutput("report", node, commandInput.getTimestamp());
            report.printCommand(output);
            return;
        }
        List<Transaction> filteredTransactions = account.
                getTransactionsInInterval(commandInput.getStartTimestamp(), commandInput.getEndTimestamp());
        PrintOutput report = new PrintOutput("report",
                account.createOutputTransactionObject(filteredTransactions),
                commandInput.getTimestamp());
        report.printCommand(output);
    }
}
