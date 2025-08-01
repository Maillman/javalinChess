package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUser(String username) throws DataAccessException;
    public void addUser(UserData user) throws DataAccessException;
    public void clearUsers() throws DataAccessException;
}