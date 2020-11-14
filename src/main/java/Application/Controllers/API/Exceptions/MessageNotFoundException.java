package Application.Controllers.API.Exceptions;

public class MessageNotFoundException extends APIException {
    public MessageNotFoundException() {
        super("Message does not exist");
    }

    public MessageNotFoundException(String message) {
        super(message);
    }
}
