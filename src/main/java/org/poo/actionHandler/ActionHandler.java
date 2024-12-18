package org.poo.actionHandler;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.BankDatabase;
import org.poo.commands.*;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;

public final class ActionHandler {

    private ActionHandler() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * method that handle the command input of the bank
     * @param output The ArrayNode where I store the JSON objects for display
     * @param bank bank database
     */
    public static void actionHandler (final ObjectInput input,
                                      final ArrayNode output,
                                      final BankDatabase bank) {
        for (CommandInput commandInput : input.getCommands()) {
            switch (commandInput.getCommand()) {
                case "printUsers" -> {
                    PrintUsers printUsers = new PrintUsers(bank, commandInput, output);
                    printUsers.execute();
                }
                case "addAccount" -> {
                    AddAccount addAccount = new AddAccount(bank, commandInput);
                    addAccount.execute();
                }
                case "createCard" -> {
                    CreateCard createCard = new CreateCard(bank, commandInput);
                    createCard.execute();
                }
                case "addFunds" -> {
                    AddFunds addFunds = new AddFunds(bank, commandInput);
                    addFunds.execute();
                }
                case "createOneTimeCard" -> {
                    CreateOneTimeCard command = new CreateOneTimeCard(bank, commandInput, commandInput.getAccount());
                    command.execute();
                }
                case "deleteCard" -> {
                    DeleteCard deleteCard = new DeleteCard(bank, commandInput, commandInput.getCardNumber());
                    deleteCard.execute();
                }
                case "deleteAccount" -> {
                    DeleteAccount deleteAccount = new DeleteAccount(bank, commandInput, output);
                    deleteAccount.execute();
                }
                case "payOnline" -> {
                    PayOnline payOnline = new PayOnline(bank, commandInput, output);
                    payOnline.execute();
                }
                case "sendMoney"-> {
                    SendMoney sendMoney = new SendMoney(bank, commandInput);
                    sendMoney.execute();
                }
                case "printTransactions" -> {
                    PrintTransaction printTransaction = new PrintTransaction(bank, commandInput, output);
                    printTransaction.execute();
                }
                case "setAlias" -> {
                    SetAlias setAlias = new SetAlias(bank, commandInput);
                    setAlias.execute();
                }
                case "checkCardStatus"-> {
                    CheckCardStatus checkCardStatus = new CheckCardStatus(bank, commandInput, output);
                    checkCardStatus.execute();
                }
                case "setMinimumBalance"-> {
                    SetMinimumBalance setMinimumBalance = new SetMinimumBalance(bank, commandInput);
                    setMinimumBalance.execute();
                }
                case "changeInterestRate"-> {
                    ChangeInterestRate changeInterestRate = new ChangeInterestRate(bank, commandInput, output);
                    changeInterestRate.execute();
                }
                case "addInterest" -> {
                    AddInterest addInterest = new AddInterest(bank, commandInput, output);
                    addInterest.execute();
                }
                case "splitPayment"-> {
                    SplitPayment splitPayment = new SplitPayment(bank, commandInput);
                    splitPayment.execute();
                }
                case "report"-> {
                   Report report = new Report(bank, commandInput, output);
                   report.execute();
                }
                case "spendingsReport"-> {
                    SpendingsReport spendingsReport = new SpendingsReport(bank, commandInput, output);
                    spendingsReport.execute();
                }
            }
        }
    }
}

