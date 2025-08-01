package service;

import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(user.username()==null||user.password()==null||user.email()==null){
            throw new DataAccessException("Error: At least one field was empty, make sure all fields are filled", 400);
        }
        //Make sure username isn't taken
        UserData existingUser = this.userDAO.getUser(user.username());
        if(existingUser!=null){
            throw new DataAccessException("Error: User already exists", 403);
        }
        //Hash password
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        UserData hashedUser = new UserData(user.username(), hashedPassword, user.email());
        //Register and authenticate them
        this.userDAO.addUser(hashedUser);
        AuthData authData = generateAuthData(user.username());
        this.authDAO.addAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws DataAccessException {
        if(user.username()==null||user.password()==null){
            throw new DataAccessException("Error: At least one field was empty, make sure all fields are filled", 400);
        }
        UserData existingUser = this.userDAO.getUser(user.username());
        if(existingUser==null){
            throw new DataAccessException("Error: Username doesn't exist", 401);
        }
        if(!checkPassword(user.password(), existingUser.password())){
            throw new DataAccessException("Error: Password incorrect", 401);
        }
        AuthData authData = generateAuthData(user.username());
        this.authDAO.addAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        if(this.authDAO.getAuth(authToken)==null){
            throw new DataAccessException("Error: Invalid authtoken", 401);
        }
        this.authDAO.deleteAuth(authToken);
    }

    private boolean checkPassword(String providedPass, String existingPass) {
        return BCrypt.checkpw(providedPass, existingPass);
    }

    private AuthData generateAuthData(String username) {
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }
}