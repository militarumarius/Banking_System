package org.poo.bank.accounts;

import org.poo.fileio.CommandInput;

import static org.poo.utils.Utils.generateIBAN;

public class FactoryAccount {
        public static Account createAccount(CommandInput input){
            switch(input.getAccountType()){
                case "classic" -> {
                    return new BasicAccount(generateIBAN(), input.getAccountType(), input.getCurrency());
                }
                case "savings" -> {
                    return new EconomyAccount(generateIBAN(), input.getAccountType(), input.getCurrency(), input.getInterestRate());
                }
                default -> {
                    return null;
                }
            }
    }
    public static Account createCopyAccount(Account account){
            switch (account.getType()){
                case "classic" -> {
                    return new BasicAccount((BasicAccount) account);
                }
                case "savings" -> {
                    return new EconomyAccount((EconomyAccount) account);
                }
                default -> {
                    return null;
                }
            }
    }
}




