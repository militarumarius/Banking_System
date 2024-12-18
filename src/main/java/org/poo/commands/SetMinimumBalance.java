package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.actionHandler.ErrorDescription;
import org.poo.actionHandler.ErrorOutput;
import org.poo.actionHandler.PrintOutput;
import org.poo.bank.BankDatabase;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Transaction;
import org.poo.transaction.TransactionBuilder;
import org.poo.transaction.TransactionDescription;

public class SetMinimumBalance implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    public SetMinimumBalance(final BankDatabase bank, final CommandInput commandInput){
        this.bank = bank;
        this.commandInput = commandInput;
    }

    @Override
    public void execute() {
        Account account = bank.findUser(commandInput.getAccount());
        account.setMinAmount(commandInput.getMinBalance());
    }
}