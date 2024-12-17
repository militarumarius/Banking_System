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

public class AddFunds implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    private final ArrayNode output;
    public AddFunds(BankDatabase bank, CommandInput commandInput, ArrayNode output){
        this.bank = bank;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        for(User user : bank.getUsers()) {
            Account account = user.findAccount(commandInput.getAccount());
            if(account != null)
                account.setBalance(account.getBalance() + commandInput.getAmount());
        }
    }
}