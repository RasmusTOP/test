package starter;

import java.util.*;

public class User {
    protected String name;
    protected String password;
    HashMap<String, Medie> myList;

    User(String name, String password) {
        this.name = name;
        this.password = password;
        myList = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }



}