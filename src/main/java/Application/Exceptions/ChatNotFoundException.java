package Application.Exceptions;

public class ChatNotFoundException extends APIException {
    public ChatNotFoundException() {
        super("Chat with this id does not exist.");
    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}
