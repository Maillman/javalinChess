package dataaccess;

import java.util.Map;

import com.google.gson.Gson;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private final int statusCode;
    public DataAccessException(String message) {
        super(message);
        this.statusCode = 500;
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
        this.statusCode = 500;
    }
    public DataAccessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage())); //Don't like that we are dependent on GSON...
    }
    public int statusCode() {
        return this.statusCode;
    }
}
