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

public class AddAccount implements Commands{
    private final BankDatabase bank;
    private final CommandInput commandInput;
    private final ArrayNode output;
    public AddAccount(BankDatabase bank, CommandInput commandInput, ArrayNode output){
        this.bank = bank;
        this.commandInput = commandInput;
        this.output = output;
    }

    @Override
    public void execute() {
        User user = bank.getUserMap().get(commandInput.getEmail());
        if(user == null)
            return;
        Account account = FactoryAccount.createAccount(commandInput);
        user.getAccounts().add(account);
        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(), TransactionDescription.ACCOUNT_CREATION_SUCCESS.getMessage())
                .build();
        account.getTransactions().add(transaction);
    }
}
