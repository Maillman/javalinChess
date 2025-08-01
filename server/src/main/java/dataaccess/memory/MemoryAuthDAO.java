package dataaccess.memory;

import java.util.HashMap;

import dataaccess.AuthDAO;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData getAuth(String authToken) {
        return this.auths.get(authToken);
    }

    @Override
    public void addAuth(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public void clearAuths() {
        this.auths.clear();
    }
    
}