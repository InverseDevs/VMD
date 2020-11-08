package Application.Controllers.API.Exceptions;

public class NoUserFoundException extends APIException {
    public NoUserFoundException(String message) {
        super(message);
    }

    public NoUserFoundException() {
        super("User with this id does not exist");
    }
}
