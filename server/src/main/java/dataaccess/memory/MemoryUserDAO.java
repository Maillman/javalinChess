package dataaccess.memory;

import java.util.HashMap;

import dataaccess.UserDAO;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void addUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public void clearUsers() {
        this.users.clear();
    }
    
}