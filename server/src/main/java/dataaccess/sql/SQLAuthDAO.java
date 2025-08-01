package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths WHERE authtoken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        String username = rs.getString("username");
                        AuthData foundAuth = new AuthData(authToken, username);
                        return foundAuth;
                    }
                }
            }
        } catch(Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error getting auth: " + ex.getMessage(), 500);
        }
        return null;
    }

    @Override
    public void addAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auths (authtoken, username) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error creating auth: " + ex.getMessage(), 500);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auths WHERE authtoken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.executeUpdate();
            }
        } catch(Exception ex) {
            System.out.println(ex);
            throw new DataAccessException("Error deleting auth: " + ex.getMessage(), 500);
        }
    }

    @Override
    public void clearAuths() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE auths";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}