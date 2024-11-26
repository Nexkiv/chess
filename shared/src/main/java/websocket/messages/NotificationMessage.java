package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage {

    private final String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
