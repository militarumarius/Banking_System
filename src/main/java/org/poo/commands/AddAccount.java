package org.poo.commands;

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
    public AddAccount(BankDatabase bank, CommandInput commandInput){
        this.bank = bank;
        this.commandInput = commandInput;
    }

    @Override
    public void execute() {
        User user = bank.getUserMap().get(commandInput.getEmail());
        if(user == null)
            return;
        Account account = FactoryAccount.createAccount(commandInput);
        if (account == null)
            return;
        user.addAccount(account);
        Transaction transaction = new TransactionBuilder(commandInput.getTimestamp(),
                TransactionDescription.ACCOUNT_CREATION_SUCCESS.getMessage())
                .build();
        account.addTransaction(transaction);
    }
}
