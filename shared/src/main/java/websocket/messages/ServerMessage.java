package websocket.messages;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    public static class ServerMessageAdapter implements JsonDeserializer<ServerMessage> {

        @Override
        public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext ctx) throws JsonParseException {
            ServerMessage serverMessage = null;
            if (jsonElement.isJsonObject()) {
                String serverMessageType = jsonElement.getAsJsonObject().get("commandType").getAsString();
                switch(ServerMessage.ServerMessageType.valueOf(serverMessageType)) {
                    case LOAD_GAME -> serverMessage = ctx.deserialize(jsonElement, LoadGameMessage.class);
                    case ERROR -> serverMessage = ctx.deserialize(jsonElement, ErrorMessage.class);
                    case NOTIFICATION -> serverMessage = ctx.deserialize(jsonElement, NotificationMessage.class);
                }
            }
            return serverMessage;
        }
    }
}
