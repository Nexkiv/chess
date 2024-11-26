package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {

    private final String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = "Error: " + errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
