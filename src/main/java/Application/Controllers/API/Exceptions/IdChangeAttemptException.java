package Application.Controllers.API.Exceptions;

public class IdChangeAttemptException extends APIException {
    public IdChangeAttemptException() { super("IDs and usernames cannot be changed."); }
}
