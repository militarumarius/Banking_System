package org.poo.bank;

import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

import static org.poo.utils.Utils.resetRandom;

public class BankDatabase {
    final private List<User> users;
    public BankDatabase(ObjectInput input) {
        resetRandom();
        users =  new ArrayList<>();
        for(UserInput user : input.getUsers())
            users.add(new User(user));
    }

    public List<User> getUsers() {
        return users;
    }
}
