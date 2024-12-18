package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.actionHandler.ErrorDescription;
import org.poo.actionHandler.ErrorOutput;
import org.poo.actionHandler.PrintOutput;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;

public class AddInterest implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    private final ArrayNode output;
    public AddInterest(BankDatabase bank, CommandInput commandInput, ArrayNode output){
        this.bank = bank;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        Account account = bank.findUser(commandInput.getAccount());
        if(!bank.checkSaving(account)){
            ErrorOutput errorOutput = new ErrorOutput(ErrorDescription
                    .INVALID_ACCOUNT.getMessage(), commandInput.getTimestamp());
            ObjectNode node = errorOutput.toObjectNodeDescription();
            PrintOutput addInterest = new PrintOutput("addInterest", node, commandInput.getTimestamp());
            addInterest.printCommand(output);
            return;
        }
        account.addBalance(account.getBalance() * commandInput.getInterestRate());
    }
}