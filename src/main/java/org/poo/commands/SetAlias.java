package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.BankDatabase;
import org.poo.bank.User;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.FactoryAccount;
import org.poo.fileio.CommandInput;
import org.poo.transaction.Transaction;
import org.poo.transaction.TransactionBuilder;
import org.poo.transaction.TransactionDescription;

public class SetAlias implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    public SetAlias(BankDatabase bank, CommandInput commandInput){
        this.bank = bank;
        this.commandInput = commandInput;
    }

    @Override
    public void execute() {
        User user = bank.getUserMap().get(commandInput.getEmail());
        Account account = user.findAccount(commandInput.getAccount());
        bank.getAliasMap().put(commandInput.getAlias(), account);
    }
}
