package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

public class SQLUserDAO implements UserDAO {

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        UserData foundUser = new UserData(username, password, email);
                        return foundUser;
                    }
                }
            }
        } catch(Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error getting user: " + ex.getMessage(), 500);
        }
        return null;
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error creating user: " + ex.getMessage(), 500);
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE users";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error clearing users: " + ex.getMessage(), 500);
        }
    }
}