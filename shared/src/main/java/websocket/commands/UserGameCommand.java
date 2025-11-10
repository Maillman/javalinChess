package websocket.commands;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand that)) {
            return false;
        }
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }

    public static class UserGameCommandAdapter implements JsonDeserializer<UserGameCommand> {

        @Override
        public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            UserGameCommand userGameCommand = null;
            if (jsonElement.isJsonObject()) {
                String commandType = jsonElement.getAsJsonObject().get("commandType").getAsString();
                switch(CommandType.valueOf(commandType)) {
                    case CONNECT -> userGameCommand = ctx.deserialize(jsonElement, ConnectCommand.class);
                    case MAKE_MOVE -> userGameCommand = ctx.deserialize(jsonElement, MakeMoveCommand.class);
                    case LEAVE -> userGameCommand = ctx.deserialize(jsonElement, UserGameCommand.class);
                    case RESIGN -> userGameCommand = ctx.deserialize(jsonElement, ResignCommand.class);
                }
            }
            return userGameCommand;
        }
    }
}
