package Application.Exceptions;

public class IdChangeAttemptException extends APIException {
    public IdChangeAttemptException() { super("IDs and usernames cannot be changed."); }
}
